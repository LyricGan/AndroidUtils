package com.lyricgan.util;

import java.security.MessageDigest;

/**
 * md5加密工具类
 * @author Lyric Gan
 */
public class Md5Utils {

    private Md5Utils() {
    }

    public static String md5Encode(String text) {
        return md5Encode(text.getBytes());
    }

    public static String md5Encode(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] digest = md.digest();
            String text;
            for (byte b : digest) {
                text = Integer.toHexString(0xFF & b);
                if (text.length() < 2) {
                    text = "0" + text;
                }
                hexString.append(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }
}
