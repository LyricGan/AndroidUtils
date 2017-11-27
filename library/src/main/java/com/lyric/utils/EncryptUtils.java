package com.lyric.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具类
 * @author lyricgan
 * @date 2017/11/24 11:24
 */
public class EncryptUtils {

    /**
     * 进行DES加密
     * @param encryptString 加密字符串
     * @param encryptKey    加密key
     * @return DES加密后字符串
     */
    public static String encryptDES(String encryptString, String encryptKey) throws Exception {
        byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    /**
     * 进行DES解密
     * @param decryptString 解密字符串
     * @param decryptKey    解密key
     * @return DES解密后字符串
     */
    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
        byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte[] byteMi = Base64.decode(decryptString, Base64.DEFAULT);
        byte decryptedData[] = cipher.doFinal(byteMi);
        return new String(decryptedData);
    }

    /**
     * 获取字符串MD5值
     * @param value 字符串
     * @param isUpperCase 是否为大写标识
     * @return 转换过的字符串
     */
    public static String md5(String value, boolean isUpperCase) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        String md5String = null;
        // 16进制字符数组
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());
            // MD5的计算结果是128位的长整数，用字节表示就是16个字节
            byte[] tmp = md.digest();
            char[] charArray = new char[16 * 2];
            // 表示转换结果中对应的字符位置
            int pos = 0;
            for (int i = 0; i < 16; i++) {
                byte ch = tmp[i];
                charArray[pos++] = hexDigits[ch >>> 4 & 0xf];
                charArray[pos++] = hexDigits[ch & 0xf];
            }
            Locale locale = Locale.getDefault();
            if (isUpperCase) {
                md5String = new String(charArray).toUpperCase(locale);
            } else {
                md5String = new String(charArray).toLowerCase(locale);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5String;
    }
}
