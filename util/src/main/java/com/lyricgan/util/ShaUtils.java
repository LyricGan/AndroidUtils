package com.lyricgan.util;

import java.security.MessageDigest;

/**
 * SHA算法工具类
 * @author Lyric Gan
 */
public class ShaUtils {

    private ShaUtils() {
    }

    public static String getSha1(String str, boolean upperCase) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            digest.update(str.getBytes("UTF-8"));
            byte[] bytes = digest.digest();
            int length = bytes.length;
            char[] buf = new char[length * 2];
            int k = 0;
            for (byte item : bytes) {
                buf[k++] = hexDigits[item >>> 4 & 0xf];
                buf[k++] = hexDigits[item & 0xf];
            }
            if (upperCase) {
                return new String(buf).toUpperCase();
            }
            return new String(buf).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSha1(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            byte[] result = digest.digest(input);
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
