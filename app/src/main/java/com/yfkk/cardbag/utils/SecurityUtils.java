package com.yfkk.cardbag.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.DisplayMetrics;

import com.yfkk.cardbag.config.Config;
import com.yfkk.cardbag.log.LogUtils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SecurityUtils {

    /**
     * 来源boss直聘(面具隐藏root时无法检测到)
     */
    public static boolean isRoot() {
        for (String file : new String[]{"/su", "/su/bin/su", "/sbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/data/local/su", "/system/xbin/su", "/system/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/system/bin/cufsdosck", "/system/xbin/cufsdosck", "/system/bin/cufsmgr", "/system/xbin/cufsmgr", "/system/bin/cufaevdd", "/system/xbin/cufaevdd", "/system/bin/conbb", "/system/xbin/conbb"}) {
            if (new File(file).exists()) {
                LogUtils.e(file);
                return true;
            }
        }
        return false;
    }

    public static boolean isVirtual(Context context) {
        return false;
    }

    /**
     * 来源boss直聘(在面具里可以检测到,vxp里无法检测)
     */
    public static boolean isHook() {
        BufferedReader bufferedReader = null;
        String readLine;
        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/self/maps"));
            do {
                readLine = bufferedReader.readLine();
                if (readLine != null) {
                    if (readLine.contains("XposedBridge.jar")) {
                        break;
                    }
                } else {
                    closeReader(bufferedReader);
                    return false;
                }
            } while (!readLine.contains("libsubstrate.so"));
            closeReader(bufferedReader);
            return true;
        } catch (Exception e2) {
            closeReader(bufferedReader);
        }
        return false;
    }

    private static void closeReader(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
            }
        }
    }

    /**
     * 来源boss直聘(在面具里可以检测到,vxp里无法检测)
     */
    public static boolean isXposed() {
        BufferedReader bufferedReader = null;
        String readLine;
        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/self/maps"));
            do {
                readLine = bufferedReader.readLine();
                if (readLine == null) {
                    closeReader(bufferedReader);
                    return false;
                }
            } while (!readLine.contains("XposedBridge.jar"));
            closeReader(bufferedReader);
            return true;
        } catch (Exception e2) {
            closeReader(bufferedReader);
        }
        return false;
    }


//    public static boolean b() {
//        boolean z = i() || j() || !k();
//        return z;
//    }
//
//    private static boolean i() {
//        boolean z = Build.PRODUCT.contains("vbox") || Build.PRODUCT.contains("sdk");
//        if (!z) {
//            return Build.FINGERPRINT.startsWith("generic");
//        }
//        return z;
//    }
//
//    private static boolean j() {
//        String a2 = a("ls /dev/");
//        if (!a2.contains("vbox") && !a2.contains("qemu")) {
//            return false;
//        }
//        return true;
//    }
//
//    private static boolean k() {
//        if (Build.VERSION.SDK_INT < 23 && !b("/sys/block/mmcblk0/device/type") && !b("/sys/block/mmcblk0/device/name") && !b("/sys/block/mmcblk0/device/cid") && !l()) {
//            return false;
//        }
//        return true;
//    }


    /**
     * 判断是否为破解版（通过签名），源码来自boss直聘
     *
     * @param context
     * @return
     */
    public static boolean isRepack(Context context) {
        Signature[] signatureArr;
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            LogUtils.e("packageName : " + packageName);
            Signature[] signatures = getSignatures(context.getApplicationInfo().sourceDir);
            if (signatures == null) {
                signatureArr = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;
            } else {
                signatureArr = signatures;
            }
            for (Signature charsString : signatureArr) {
                LogUtils.e("charsString : " + Integer.toHexString(charsString.toCharsString().hashCode()));
                if (Config.SHA1_KEY.equalsIgnoreCase(Integer.toHexString(charsString.toCharsString().hashCode()))) {
                    return false;
                }
            }
            return true;
        } catch (Throwable th) {
            return true;
        }
    }

    private static Signature[] getSignatures(String str) throws Exception {
        try {
            Object c2 = newInstancePackageParser(str);
            Object a2 = invokeParsePackage(str, c2);
            c2.getClass().getDeclaredMethod("collectCertificates", new Class[]{a2.getClass(), Integer.TYPE}).invoke(c2, new Object[]{a2, 0});
            Field declaredField = a2.getClass().getDeclaredField("mSignatures");
            declaredField.setAccessible(true);
            return (Signature[]) declaredField.get(a2);
        } catch (Throwable th) {
            return null;
        }
    }

    private static Object invokeParsePackage(String str, Object obj) throws Exception {
        if (Build.VERSION.SDK_INT >= 21) {
            Method declaredMethod = obj.getClass().getDeclaredMethod("parsePackage", new Class[]{File.class, Integer.TYPE});
            Object[] objArr = {new File(str), 0};
            declaredMethod.setAccessible(true);
            return declaredMethod.invoke(obj, objArr);
        }
        Method declaredMethod2 = obj.getClass().getDeclaredMethod("parsePackage", new Class[]{File.class, String.class, DisplayMetrics.class, Integer.TYPE});
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.setToDefaults();
        Object[] objArr2 = {new File(str), str, displayMetrics, 0};
        declaredMethod2.setAccessible(true);
        return declaredMethod2.invoke(obj, objArr2);
    }


    private static Object newInstancePackageParser(String str) throws Exception {
        Class<?> cls = Class.forName("android.content.pm.PackageParser");
        if (Build.VERSION.SDK_INT >= 21) {
            return cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
        return cls.getDeclaredConstructor(new Class[]{String.class}).newInstance(new Object[]{str});
    }


}
