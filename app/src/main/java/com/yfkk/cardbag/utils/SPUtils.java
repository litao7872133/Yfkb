package com.yfkk.cardbag.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yfkk.cardbag.MainApplication;

/**
 * 键值对保存简单数据
 */
public class SPUtils {
    private static String SP_NAME = "sp";
    private static SharedPreferences sp;

    //保存布尔值
    public static void saveBoolean(String key, boolean value) {
        if (sp == null) {
            sp = MainApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        if (sp == null) {
            sp = MainApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        boolean result = sp.getBoolean(key, defValue);
        return result;
    }

    //保存字符串（值进行了加解密，也可以使用 https://github.com/iamMehedi/Secured-Preference-Store 进行加密）
    public static void saveString(String key, String value) {
        if (sp == null) {
            sp = MainApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, StringUtils.isEmpty(value) ? "" : CryptoUtils.AES_Encrypt(value)).apply();
    }

    public static String getString(String key, String defValue) {
        if (sp == null) {
            sp = MainApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        String value = sp.getString(key, defValue);
        return CryptoUtils.AES_Decrypt(value);
    }

    //保存Long---token
    public static void saveLong(String key, long value) {
        if (sp == null) {
            sp = MainApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putLong(key, value).apply();
    }

    public static long getLong(String key, long defValue) {
        if (sp == null) {
            sp = MainApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        long result = sp.getLong(key, defValue);
        return result;
    }

    //保存int
    public static void saveInt(String key, int value) {
        if (sp == null) {
            sp = MainApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        if (sp == null) {
            sp = MainApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        int result = sp.getInt(key, defValue);
        return result;
    }

}
