package com.yfkk.cardbag.utils;

import android.util.Base64;


import com.yfkk.cardbag.config.Config;
import com.yfkk.cardbag.log.LogUtils;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加解密
 */
public class CryptoUtils {

    /**
     * AES 字符串加密（加密后的字符并不是固定值）
     *
     * @param content
     * @throws Exception
     */
    public static String AES_Encrypt(String content){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            //还原密钥
            SecretKey key = new SecretKeySpec(Base64.decode(Config.AES_KEY, Base64.DEFAULT), "AES");
            //加密
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] relBytes = cipher.doFinal(content.getBytes());
            //注意：加密过后用Base64编码 缺少这步会导致解密失败
            byte[] relBase = Base64.encode(relBytes, Base64.DEFAULT);
            String encodeStr = new String(relBase);
            return encodeStr;
        } catch (Exception e) {

        }
        return content;
    }

    /**
     * AES 解密
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static String AES_Decrypt(String content){
        try {
            //还原密钥
            SecretKey key = new SecretKeySpec(Base64.decode(Config.AES_KEY, Base64.DEFAULT), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            //4，执行解密
            //先用Base64解密 缺少Base64编码会导致解密失败
            byte[] decode = Base64.decode(content, Base64.DEFAULT);
            byte[] bytes = cipher.doFinal(decode);
            String decodeStr = new String(bytes);
            return decodeStr;
        } catch (Exception e) {

        }
        return content;
    }

    /**
     * 使用DES对字符串加密
     *
     * @param str utf8编码的字符串
     * @param key 密钥（56位，7字节）
     */
    public static String desEncrypt(String str, String key) throws Exception {
        if (str == null || key == null)
            return null;
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "DES"));
        byte[] bytes = cipher.doFinal(str.getBytes("utf-8"));
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 使用DES对数据解密
     *
     * @param key 密钥（16字节）
     * @return 解密结果
     * @throws Exception
     */
    public static String desDecrypt(String str, String key) throws Exception {
        byte[] bytes = Base64.decode(str, Base64.DEFAULT);
        if (bytes == null || key == null)
            return null;
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), "DES"));
        bytes = cipher.doFinal(bytes);
        return new String(bytes, "utf-8");
    }

    /**
     * AES 密钥生成
     *
     * @throws NoSuchAlgorithmException
     */
    public static void creatKeyAES() throws NoSuchAlgorithmException {
        //生成KEY
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        //产生密钥
        SecretKey secretKey = keyGenerator.generateKey();
        //获取密钥
        byte[] keyBytes = secretKey.getEncoded();
        LogUtils.e("AES KEY : " + Base64.encodeToString(keyBytes, 0));
    }

}
