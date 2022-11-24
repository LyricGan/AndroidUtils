package com.lyricgan.util;

import android.text.TextUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 字符串工具类
 * @author Lyric Gan
 */
public class StringUtils {

    private StringUtils() {
    }

    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    public static String emptyString(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    public static int parseInt(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int parseInt(String value, int defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        int valueInt = defaultValue;
        try {
            valueInt = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            try {
                valueInt = Double.valueOf(value).intValue();
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        }
        return valueInt;
    }

    public static float parseFloat(String value, float defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        float valueFloat = defaultValue;
        try {
            valueFloat = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return valueFloat;
    }

    public static double parseDouble(String value, double defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        double valueDouble = defaultValue;
        try {
            valueDouble = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return valueDouble;
    }

    public static long parseLong(String value, long defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        long valueLong = defaultValue;
        try {
            valueLong = Long.parseLong(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return valueLong;
    }

    public static short parseShort(String value, short defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        short valueShort = defaultValue;
        try {
            valueShort = Short.parseShort(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return valueShort;
    }

    public static String combineString(String... strings) {
        if (strings == null || strings.length <= 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String arg : strings) {
            builder.append(arg);
        }
        return builder.toString();
    }

    public static <T> String toString(List<T> list) {
        StringBuilder builder = new StringBuilder();
        for (T item : list) {
            builder.append(item.toString()).append("\n");
        }
        return builder.toString();
    }

    public static <K, V> String toString(Map<K, V> map) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<K, V> item : map.entrySet()) {
            builder.append(item.getKey()).append("=").append(item.getValue()).append("\n");
        }
        return builder.toString();
    }

    /**
     * 对于浮点数进行格式化
     * @param number 浮点数
     * @param decimalsCount 保留的小数位数
     * @param isRetainInteger 如果是整数是否保留小数点后面的0
     * @return 格式化字符串
     */
    public static String formatDecimals(double number, int decimalsCount, boolean isRetainInteger) {
        if (isRetainInteger) {
            if (Math.round(number) - number == 0) {
                return number + "";
            }
            return String.format(Locale.CHINESE, "%1$." + decimalsCount + "f", number);
        }
        return String.format(Locale.CHINESE, "%1$." + decimalsCount + "f", number);
    }
}