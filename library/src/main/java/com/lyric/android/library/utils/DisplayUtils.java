package com.lyric.android.library.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * @author lyric
 * @description
 * @time 2016/3/12 15:04
 */
public class DisplayUtils {

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

    public static int[] getScreenDisplay(Context context) {
        checkContext(context);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int[] display = new int[2];
        display[0] = metrics.widthPixels;
        display[1] = metrics.heightPixels;

        return display;
    }

    public static int getScreenWidth(Context context) {
        checkContext(context);
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
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

    /**
     * To get the real screen resolution includes the system status bar.
     * We can get the value by calling the getRealMetrics method if API >= 17
     * Reflection needed on old devices.
     *
     * @return a pair to return the width and height
     */
    public static Pair<Integer, Integer> getResolution(Context context) {
        checkContext(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getRealResolution(context);
        } else {
            return getRealResolutionOnOldDevice(context);
        }
    }

    /**
     * Gets resolution on old devices.
     * Tries the reflection to get the real resolution first.
     * Fall back to getDisplayMetrics if the above method failed.
     */
    private static Pair<Integer, Integer> getRealResolutionOnOldDevice(Context context) {
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Method mGetRawWidth = Display.class.getMethod("getRawWidth");
            Method mGetRawHeight = Display.class.getMethod("getRawHeight");
            Integer realWidth = (Integer) mGetRawWidth.invoke(display);
            Integer realHeight = (Integer) mGetRawHeight.invoke(display);
            return new Pair<>(realWidth, realHeight);
        } catch (Exception e) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return new Pair<>(metrics.widthPixels, metrics.heightPixels);
        }
    }

    /**
     * Gets real resolution via the new getRealMetrics API.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Pair<Integer, Integer> getRealResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getRealMetrics(metrics);
        return new Pair<>(metrics.widthPixels, metrics.heightPixels);
    }
}
