package com.lyricgan.util;

import android.graphics.Color;

/**
 * 颜色工具类
 * @author Lyric Gan
 */
public class ColorUtils {

    private ColorUtils() {
    }

    public static int parseColor(String colorString) {
        try {
            return Color.parseColor(colorString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Color.TRANSPARENT;
    }

    public static String convertRgb(int colorInt) {
        colorInt = colorInt & 0x00ffffff;
        StringBuilder color = new StringBuilder(Integer.toHexString(colorInt));
        while (color.length() < 6) {
            color.insert(0, "0");
        }
        return "#" + color;
    }

    public static String convertArgb(int colorInt) {
        StringBuilder color = new StringBuilder(Integer.toHexString(colorInt));
        while (color.length() < 6) {
            color.insert(0, "0");
        }
        while (color.length() < 8) {
            color.insert(0, "f");
        }
        return "#" + color;
    }

    public static boolean isLightColor(int color) {
        return 0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color) >= 127.5;
    }
}
