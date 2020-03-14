package com.yfkk.cardbag.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.yfkk.cardbag.BuildConfig;
import com.yfkk.cardbag.MainApplication;
import com.yfkk.cardbag.log.LogUtils;

/**
 * 冒泡提示
 * <p>
 * Created by litao on 2018/1/24.
 */

public class ToastUtils {

    public static void makeText(String text) {
        Toast toast = Toast.makeText(MainApplication.getInstance(), text, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void makeText(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void makeThrowable(String action, Throwable e) {
        if (e == null) {
            return;
        }
        LogUtils.e("数据异常 : " + e + " , " + Log.getStackTraceString(e));
        e.printStackTrace();
        String text = action + "数据异常";
        if (e != null && e.toString().toLowerCase().contains("http")) {
            text = "无法连接网络，请检查网络";
        } else {
            if (BuildConfig.IS_SHOW_LOG) {
                text = "数据异常";
            }
        }
        Toast toast = Toast.makeText(MainApplication.getInstance(), text, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
