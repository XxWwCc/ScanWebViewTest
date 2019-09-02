package com.xwc.ScanWebViewTest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * author: xuweichao
 * date: 2019/8/30 11:17
 * desc:
 */
public class WebViewTest extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_webview_test);
        webView = findViewById(R.id.webview);
        initView();
    }

    private void initView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        JavaScriptMetod m = new JavaScriptMetod(this, this, webView);
        webView.addJavascriptInterface(m, JavaScriptMetod.JAVAINTERFACE);

        webView.loadUrl("http://116.62.168.126/wc/");
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                String pre = "protocol://android";
//                if (!url.contains(pre)) {
//                    return false;
//                } else {
//                    Map map = getParamsMap(url, pre);
//                    String code = (String) map.get("code");
//                    String data = (String) map.get("data");
//                    parseCode(code, data);
//                    return true;
//                }
//            }
//        });
    }

    private Map getParamsMap(String url, String pre) {
        ArrayMap queryStringMap = new ArrayMap<>();
        if (url.contains(pre)) {
            int index = url.indexOf(pre);
            int end = index + pre.length();
            String queryString = url.substring(end + 1);
            String[] queryStringSplit = queryString.split("&");
            String[] queryStringParam;
            for (String qs : queryStringSplit) {
                if (qs.toLowerCase().startsWith("data=")) {
                    int dataIndex = queryString.indexOf("data=");
                    String dataValue = queryString.substring(dataIndex + 5);
                    queryStringMap.put("data", dataValue);
                } else {
                    queryStringParam = qs.split("=");
                    String value = "";
                    if (queryStringParam.length > 1) {
                        value = queryStringParam[1];
                    }
                    queryStringMap.put(queryStringParam[0].toLowerCase(), value);
                }
            }
        }
        return queryStringMap;
    }

    private void parseCode(String code, String data) {
        if (code.equals("call")) {
            try {
                JSONObject json = new JSONObject(data);
                String phone = json.optString("data");
                PhoneUtils.call(this, phone);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }

        if (code.equals("toast")) {
            try {
                JSONObject json = new JSONObject(data);
                String toast = json.optString("data");
                Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "scanned:" + result.getContents(), Toast.LENGTH_LONG).show();
                Log.e("TAG", result.getContents());
                webView.loadUrl("http://192.168.101.182:8080/wc/#/suesCode?code=" + result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static void openActivity(Context context) {
        Intent intent = new Intent(context, WebViewTest.class);
        context.startActivity(intent);
    }
}
