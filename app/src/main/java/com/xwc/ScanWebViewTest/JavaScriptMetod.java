package com.xwc.ScanWebViewTest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

/**
 * author: xuweichao
 * date: 2019/8/30 11:27
 * desc:
 */
public class JavaScriptMetod {

    private Activity mActivity;
    private Context mContext;
    private WebView mWebView;

    public static final String JAVAINTERFACE = "javaInterface";

    public JavaScriptMetod(Activity activity, Context context, WebView webView) {
        mActivity = activity;
        mContext = context;
        mWebView = webView;
    }

    @JavascriptInterface
    public void showToast(String json) {
        Toast.makeText(mContext, json, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel=" + phone);
        intent.setData(data);
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void sweepCode() {
        Toast.makeText(mActivity, "扫码", Toast.LENGTH_SHORT).show();
        new IntentIntegrator(mActivity)
                .setCaptureActivity(ScanActivity.class)
                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                .setPrompt("请对准二维码")
                .setCameraId(0) // 前置或后置
                .setBeepEnabled(false) // 是否哔一声
                .setBarcodeImageEnabled(true) // 扫码后是否生成二维码图片
                .initiateScan();
    }
}
