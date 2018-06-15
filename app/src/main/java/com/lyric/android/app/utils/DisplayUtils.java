package com.lyric.android.app.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * 视图显示工具类
 * @author lyricgan
 * @time 2016/3/12 15:04
 */
public class DisplayUtils {

    public static Resources getResources(Context context) {
        return context.getResources();
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = getDensity(context);
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = getDensity(context);
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        return dip2px(context, spValue);
    }

    public static int px2sp(Context context, float pxValue) {
        return px2dip(context, pxValue);
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return getResources(context).getDisplayMetrics();
    }

    public static float getDensity(Context context) {
        return getDisplayMetrics(context).density;
    }

    public static int getDensityDpi(Context context) {
        return getDisplayMetrics(context).densityDpi;
    }

    public static float getScaledDensity(Context context) {
        return getDisplayMetrics(context).scaledDensity;
    }

    public static int[] getScreenDisplay(Context context) {
        DisplayMetrics metrics = getDisplayMetrics(context);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    public static float getXdpi(Context context) {
        return getDisplayMetrics(context).xdpi;
    }

    public static float getYdpi(Context context) {
        return getDisplayMetrics(context).ydpi;
    }

    public static void log(Context context) {
        StringBuilder builder = new StringBuilder();
        DisplayMetrics displayMetrics = getDisplayMetrics(context);
        builder.append("density=").append(displayMetrics.density).append("\n")
                .append("densityDpi=").append(displayMetrics.densityDpi).append("\n")
                .append("scaledDensity=").append(displayMetrics.scaledDensity).append("\n")
                .append("screenWidth=").append(displayMetrics.widthPixels).append("\n")
                .append("screenHeight=").append(displayMetrics.heightPixels).append("\n")
                .append("xdpi=").append(displayMetrics.xdpi).append("\n")
                .append("ydpi=").append(displayMetrics.ydpi).append("\n");

        Log.d(DisplayUtils.class.getSimpleName(), builder.toString());
    }

    /**
     * To get the real screen resolution includes the system status bar.
     * We can get the value by calling the getRealMetrics method if API >= 17
     * Reflection needed on old devices.
     *
     * @return a pair to return the width and height
     */
    public static Pair<Integer, Integer> getResolution(Context context) {
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
            if (wm != null) {
                Display display = wm.getDefaultDisplay();
                Method mGetRawWidth = Display.class.getMethod("getRawWidth");
                Method mGetRawHeight = Display.class.getMethod("getRawHeight");
                Integer realWidth = (Integer) mGetRawWidth.invoke(display);
                Integer realHeight = (Integer) mGetRawHeight.invoke(display);
                return new Pair<>(realWidth, realHeight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return new Pair<>(metrics.widthPixels, metrics.heightPixels);
    }

    /**
     * Gets real resolution via the new getRealMetrics API.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Pair<Integer, Integer> getRealResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getRealMetrics(metrics);
            return new Pair<>(metrics.widthPixels, metrics.heightPixels);
        }
        return null;
    }

    public static float getDimension(Context context, @DimenRes int id) {
        return getResources(context).getDimension(id);
    }

    public static int getDimensionPixelOffset(Context context, @DimenRes int id) {
        return getResources(context).getDimensionPixelOffset(id);
    }

    public static int getDimensionPixelSize(Context context, @DimenRes int id) {
        return getResources(context).getDimensionPixelSize(id);
    }

    public static String getString(Context context, @StringRes int id) {
        return getResources(context).getString(id);
    }

    public static String getString(Context context, @StringRes int id, Object... formatArgs) {
        return getResources(context).getString(id, formatArgs);
    }

    public static String[] getStringArray(Context context, @ArrayRes int id) {
        return getResources(context).getStringArray(id);
    }

    public int[] getIntArray(Context context, @ArrayRes int id) {
        return getResources(context).getIntArray(id);
    }

    public static int getColor(Context context, @ColorRes int id) {
        return getResources(context).getColor(id);
    }

    public static ColorStateList getColorStateList(Context context, @ColorRes int id) {
        return getResources(context).getColorStateList(id);
    }

    public static InputStream openRawResource(Context context, @RawRes int id) {
        return getResources(context).openRawResource(id);
    }

    public static InputStream openRawResource(Context context, @RawRes int id, TypedValue value) {
        return getResources(context).openRawResource(id, value);
    }

    public static AssetManager getAssets(Context context) {
        return getResources(context).getAssets();
    }
}
