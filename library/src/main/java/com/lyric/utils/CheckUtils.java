package com.lyric.utils;

import android.content.Context;

/**
 * @author lyric
 * @description 检查工具类
 * @time 2016/6/21 11:49
 */
public class CheckUtils {

    private CheckUtils() {
    }

    public static void checkContext(Context context) {
        checkNotNull(context, "context == null");
    }

    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }
}
