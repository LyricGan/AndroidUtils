package com.lyric.android.library.utils;

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
        if (context == null) {
            throw new NullPointerException("context can not be null.");
        }
    }
}
