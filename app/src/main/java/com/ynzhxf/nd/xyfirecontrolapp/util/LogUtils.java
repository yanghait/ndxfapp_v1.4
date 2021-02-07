package com.ynzhxf.nd.xyfirecontrolapp.util;

import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.ynzhxf.nd.xyfirecontrolapp.tool.ApplicationPlatform;

public class LogUtils {
    public static void showLoge(String tag, String content) {
        if (isApkInDebug()) {
            Log.e(tag, content);
        } else {
            Log.i(tag, content);
        }
    }

    public static void showLogd(String tag, String content) {
        if (isApkInDebug()) {
            Log.d(tag, content);
        } else {
            Log.i(tag, content);
        }
    }

    public static void showLogI(String tag, Object content) {
        if (isApkInDebug()) {
            Log.i(tag, String.valueOf(content));
        }
    }

    /**
     * 判断当前应用是否是debug状态
     */
    private static boolean isApkInDebug() {
        try {
            ApplicationInfo info = ApplicationPlatform.getContext().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
