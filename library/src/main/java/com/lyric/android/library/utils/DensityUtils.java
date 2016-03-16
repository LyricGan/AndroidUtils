package com.lyric.android.library.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author lyric
 * @description
 * @time 2016/3/12 15:04
 */
public class DensityUtils {

    public static float getDensity(Context context) {
        checkContext(context);
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = getDensity(context);
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = getDensity(context);
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getDensityDpi(Context context) {
        checkContext(context);
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static float getScaledDensity(Context context) {
        checkContext(context);
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int[] getDisplay(Context context) {
        checkContext(context);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int[] display = new int[2];
        display[0] = metrics.widthPixels;
        display[1] = metrics.heightPixels;

        return display;
    }

    public static int getWidthPixels(Context context) {
        checkContext(context);
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeightPixels(Context context) {
        checkContext(context);
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static float getXdpi(Context context) {
        checkContext(context);
        return context.getResources().getDisplayMetrics().xdpi;
    }

    public static float getYdpi(Context context) {
        checkContext(context);
        return context.getResources().getDisplayMetrics().ydpi;
    }

    private static void checkContext(Context context) {
        if (context == null) {
            throw new NullPointerException("context can not be null.");
        }
    }

}
