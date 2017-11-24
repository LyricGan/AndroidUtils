package com.lyric.utils;

import android.util.Base64;

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
}
