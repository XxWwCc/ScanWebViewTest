package com.xwc.ScanWebViewTest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * author: xuweichao
 * date: 2019/8/30 14:16
 * desc:
 */
public class PhoneUtils {

    public static void call(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        context.startActivity(intent);
    }
}
