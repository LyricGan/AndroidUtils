package com.lyricgan.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * des加密，对称密钥加密工具类
 * @author Lyric Gan
 */
public class DesUtils {
    private static final byte[] IV_BYTES = {1, 2, 3, 4, 5, 6, 7, 8};

    private DesUtils() {
    }

    public static String encryptDes(String encryptString, String encryptKey) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(IV_BYTES);
            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
            return Base64.encodeToString(encryptedData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptString;
    }

    public static String decryptDes(String decryptString, String decryptKey) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(IV_BYTES);
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte[] decryptedData = cipher.doFinal(Base64.decode(decryptString, Base64.DEFAULT));

            return new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptString;
    }
}
