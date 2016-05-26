package com.lyric.android.library.utils;

import android.content.Context;

/**
 * @author lyric
 * @description 本地资源工具类
 * @time 2016/4/22 14:37
 */
public class ResourceUtils {

    ResourceUtils() {
    }

    public static int getColor(Context context, int colorId) {
        return context.getResources().getColor(colorId);
    }

    public static String getString(Context context, int stringId) {
        return context.getResources().getString(stringId);
    }

    public static String getString(Context context, int stringId, Object... formatArgs) {
        return context.getResources().getString(stringId, formatArgs);
    }
}
