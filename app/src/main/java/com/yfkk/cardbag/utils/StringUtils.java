package com.yfkk.cardbag.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.yfkk.cardbag.config.UrlConfig;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

public class StringUtils {
    /**
     * 匹配手机号
     */
    public static final String REGEX_MOBILE = "^[1][0123456789][0-9]{9}$";
    /**
     * 匹用户名密码
     */
    public static final String REGEX_PASSWORD = "\\w{6,16}$";
    /**
     * 匹用验证码
     */
    public static final String REGEX_SMS = "\\d{4,6}$";

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
//        return !isEmpty(mobile) && mobile.length() >= 3;
        return Pattern.matches(REGEX_MOBILE, mobile);
    }


    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验验证码
     *
     * @param sms
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isSMS(String sms) {
        return Pattern.matches(REGEX_SMS, sms);
    }

    /**
     * 校验名称
     *
     * @param name
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isName(String name) {
        return isEmpty(name) ? false : name.length() <= 10;
    }

    /**
     * dp转px
     */
    public static int dpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    /**
     * 通过context获得屏幕宽高
     */
    public static int getWidthPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();

        return dm.heightPixels < dm.widthPixels ? dm.heightPixels : dm.widthPixels;
    }

    public static int getHeightPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();

        return dm.heightPixels > dm.widthPixels ? dm.heightPixels : dm.widthPixels;
    }

    /**
     * double取小数后index位
     *
     * @param d
     * @return
     */
    public static String doubleFormatString(double d, int index) {
        BigDecimal bd = new BigDecimal(d);
        d = bd.setScale(index, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (d - ((int) d) == 0) {
            return String.valueOf((int) d);
        }
        return String.valueOf(d);
    }

    /**
     * 判断字符串是否为空
     *
     * @param s
     * @return 如果字符串为空，返回true；否则返回false
     */
    public static boolean isEmpty(String s) {
        return s == null || s.matches("\\s*") || s.equals("null");
    }

    /**
     * 编码
     *
     * @param str
     * @return
     */
    public static String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
        }
        return str;
    }

    /**
     * 解码
     *
     * @param str
     * @return
     */
    public static String decode(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (Exception e) {
        }
        return str;
    }


    /**
     * 拼接域名
     */
    public static String mosaicUrl(String url) {
        // 本地文件
        if (url == null || url.startsWith("/storage") || url.startsWith("/data")) {
            return url;
        }
        if (!url.startsWith("http")) {
            url = UrlConfig.HOST_URL + url;
        }
        return url;
    }

}
