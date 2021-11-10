package com.lyricgan.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * resource display utils
 *
 * @author Lyric Gan
 */
public class DisplayUtils {

    public static Resources getResources(Context context) {
        return context.getResources();
    }

    public static int dip2px(Context context, float dp) {
        float scale = getDensity(context);
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dip(Context context, float px) {
        float scale = getDensity(context);
        return (int) (px / scale + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        return dip2px(context, sp);
    }

    public static int px2sp(Context context, float px) {
        return px2dip(context, px);
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

    public static Point getScreenPoint(Context context) {
        DisplayMetrics metrics = getDisplayMetrics(context);
        return new Point(metrics.widthPixels, metrics.heightPixels);
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

    /**
     * To get the real screen resolution includes the system status bar.
     * We can get the value by calling the getRealMetrics method if API >= 17
     * Reflection needed on old devices.
     *
     * @return a pair to return the width and height
     */
    public static Pair<Integer, Integer> getResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                // Gets real resolution via the new getRealMetrics API.
                Display display = wm.getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                display.getRealMetrics(metrics);
                return new Pair<>(metrics.widthPixels, metrics.heightPixels);
            } else {
                // Gets resolution on old devices.
                // Tries the reflection to get the real resolution first.
                // Fall back to getDisplayMetrics if the above method failed.
                try {
                    Display display = wm.getDefaultDisplay();
                    Method mGetRawWidth = Display.class.getMethod("getRawWidth");
                    Method mGetRawHeight = Display.class.getMethod("getRawHeight");
                    Integer realWidth = (Integer) mGetRawWidth.invoke(display);
                    Integer realHeight = (Integer) mGetRawHeight.invoke(display);
                    return new Pair<>(realWidth, realHeight);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return new Pair<>(metrics.widthPixels, metrics.heightPixels);
    }

    public static float getDimension(Context context, int id) {
        return getResources(context).getDimension(id);
    }

    public static int getDimensionPixelOffset(Context context, int id) {
        return getResources(context).getDimensionPixelOffset(id);
    }

    public static int getDimensionPixelSize(Context context, int id) {
        return getResources(context).getDimensionPixelSize(id);
    }

    public static String getString(Context context, int id) {
        return getResources(context).getString(id);
    }

    public static String getString(Context context, int id, Object... formatArgs) {
        return getResources(context).getString(id, formatArgs);
    }

    public static String[] getStringArray(Context context, int id) {
        return getResources(context).getStringArray(id);
    }

    public int[] getIntArray(Context context, int id) {
        return getResources(context).getIntArray(id);
    }

    public static int getColor(Context context, int id) {
        return getResources(context).getColor(id);
    }

    public static ColorStateList getColorStateList(Context context, int id) {
        return getResources(context).getColorStateList(id);
    }

    public static AssetManager getAssets(Context context) {
        return getResources(context).getAssets();
    }

    public static String toString(Context context) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        Pair<Integer, Integer> pair = getResolution(context);
        return "DisplayMetrics [density=" + displayMetrics.density + "," +
                "densityDpi=" + displayMetrics.densityDpi + "," +
                "scaledDensity=" + displayMetrics.scaledDensity + "," +
                "widthPixels=" + displayMetrics.widthPixels + "," +
                "heightPixels=" + displayMetrics.heightPixels + "," +
                "xdpi=" + displayMetrics.xdpi + "," +
                "ydpi=" + displayMetrics.ydpi + "," +
                "resolution widthPixels=" + pair.first + "," +
                "resolution heightPixels=" + pair.second +
                "]";
    }
}
