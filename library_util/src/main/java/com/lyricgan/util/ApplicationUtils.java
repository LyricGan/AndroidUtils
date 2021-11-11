package com.lyricgan.util;

import android.app.Application;

import java.lang.reflect.Field;

/**
 * 应用管理工具类
 * @author Lyric Gan
 */
public class ApplicationUtils {
    private static Application sApplication;

    private ApplicationUtils() {
    }

    public static void setApplication(Application application) {
        sApplication = application;
    }

    public static Application getApplication() {
        if (sApplication == null) {
            setApplication(getApplicationByReflect());
        }
        return sApplication;
    }

    private static Application getApplicationByReflect() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Object thread = getActivityThread();
            Object app = activityThreadClass.getMethod("getApplication").invoke(thread);
            if (app == null) {
                return null;
            }
            return (Application) app;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getActivityThread() {
        Object activityThread = getActivityThreadInActivityThreadStaticField();
        if (activityThread != null) {
            return activityThread;
        }
        return getActivityThreadInActivityThreadStaticMethod();
    }

    private static Object getActivityThreadInActivityThreadStaticField() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            return sCurrentActivityThreadField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getActivityThreadInActivityThreadStaticMethod() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            return activityThreadClass.getMethod("currentActivityThread").invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
