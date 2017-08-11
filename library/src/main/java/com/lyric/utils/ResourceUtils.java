package com.lyric.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * @author lyric
 * @description 本地资源工具类
 * @time 2016/4/22 14:37
 */
public class ResourceUtils {
    private static Resources mResources;

    private ResourceUtils() {
    }

    private static void ensureResources(Context context) {
        if (mResources == null) {
            mResources = context.getApplicationContext().getResources();
        }
    }

    public static int getColor(Context context, int colorId) {
        ensureResources(context);
        return mResources.getColor(colorId);
    }

    public static String getString(Context context, int stringId) {
        ensureResources(context);
        return mResources.getString(stringId);
    }

    public static String getString(Context context, int stringId, Object... formatArgs) {
        ensureResources(context);
        return mResources.getString(stringId, formatArgs);
    }

    public static CharSequence getText(Context context, int id) {
        return getText(context, id, "");
    }

    public static CharSequence getText(Context context, int id, CharSequence def) {
        ensureResources(context);
        return mResources.getText(id, def);
    }

    public static float getDimension(Context context, int id) {
        ensureResources(context);
        return mResources.getDimension(id);
    }

    public static int getDimensionPixelOffset(Context context, int id) {
        ensureResources(context);
        return mResources.getDimensionPixelOffset(id);
    }

    public static int getDimensionPixelSize(Context context, int id) {
        ensureResources(context);
        return mResources.getDimensionPixelSize(id);
    }

    public static Drawable getDrawable(Context context, int id) {
        ensureResources(context);
        return mResources.getDrawable(id);
    }

}
