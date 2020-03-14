package com.yfkk.cardbag.log;

import android.util.Log;

import com.yfkk.cardbag.BuildConfig;


/**
 * 日志工具类
 * <p>
 * Created by litao on 2018/1/16.
 */
public class LogUtils {

    public static boolean isDebug = BuildConfig.IS_SHOW_LOG;
    private static final String TAG = "YFKB";

    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        longString(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }

    public static void longString(String tag, String str) {
        if (isDebug) {
            str = str.trim();
            int index = 0;
            int maxLength = 4000;
            String sub;
            while (index < str.length()) {
                // java的字符不允许指定超过总的长度end
                if (str.length() <= index + maxLength) {
                    sub = str.substring(index);
                } else {
                    sub = str.substring(index, maxLength + index);
                }

                index += maxLength;
                Log.e(tag, sub.trim());
            }
        }
    }

}