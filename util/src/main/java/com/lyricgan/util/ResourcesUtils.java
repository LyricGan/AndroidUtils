package com.lyricgan.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 资源管理工具类
 * @author Lyric Gan
 */
public class ResourcesUtils {

    private ResourcesUtils() {
    }

    public static Resources getResources() {
        return ApplicationUtils.getContext().getResources();
    }

    public static Resources getSystem() {
        return Resources.getSystem();
    }

    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(final float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(final float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(final float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static float applyDimension(final float value, final int unit) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                return value;
            case TypedValue.COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case TypedValue.COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case TypedValue.COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f / 72);
            case TypedValue.COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case TypedValue.COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f / 25.4f);
        }
        return 0;
    }

    public static int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return resources.getDimensionPixelSize(resourceId);
        } else {
            try {
                Class<?> cls = Class.forName("com.android.internal.R$dimen");
                Object obj = cls.newInstance();
                Field field = cls.getField("status_bar_height");
                int x = (Integer) field.get(obj);
                return resources.getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static int getNavigationBarHeight() {
        if (hasNavigationBar()) {
            Resources resources = getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId != 0) {
                return resources.getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public static boolean hasNavigationBar() {
        boolean result = false;
        Resources rs = getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            result = rs.getBoolean(id);
        }
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                result = false;
            } else if ("0".equals(navBarOverride)) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Resources adaptFontScale(Context context, Resources resources) {
        if (context == null || resources == null) {
            return resources;
        }
        Configuration configuration = resources.getConfiguration();
        if (configuration.fontScale > 1.0f) {
            configuration.fontScale = 1.0f;
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            displayMetrics.scaledDensity = displayMetrics.density * configuration.fontScale;
            return context.createConfigurationContext(configuration).getResources();
        }
        return resources;
    }

    public static String toDisplayString(DisplayMetrics metrics) {
        return "DisplayMetrics [density=" + metrics.density + "," +
                "densityDpi=" + metrics.densityDpi + "," +
                "scaledDensity=" + metrics.scaledDensity + "," +
                "widthPixels=" + metrics.widthPixels + "," +
                "heightPixels=" + metrics.heightPixels + "," +
                "xdpi=" + metrics.xdpi + "," +
                "ydpi=" + metrics.ydpi +
                "]";
    }
}
