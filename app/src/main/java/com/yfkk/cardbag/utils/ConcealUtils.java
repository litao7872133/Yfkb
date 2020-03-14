package com.yfkk.cardbag.utils;

public class ConcealUtils {

    public static String encrypt(String data) {
        //把字符串转为字节数组
        byte[] b = data.getBytes();
        //遍历
        for (int i = 0; i < b.length; i++) {
            b[i] += 1;//在原有的基础上+1
        }
        return new String(b);
    }

    public static String decrypt(String data) {
        byte[] b = data.getBytes();
        for (int i = 0; i < b.length; i++) {
            b[i] -= 1;
        }
        return new String(b);
    }
}
