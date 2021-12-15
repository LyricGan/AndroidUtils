package com.lyricgan.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.lang.reflect.Field;

/**
 * 应用管理工具类
 * @author Lyric Gan
 */
public class ApplicationUtils {
    private static Application sApplication;
    /**
     * 当前是否为调试模式标识
     */
    private static Boolean sDebugMode;

    private ApplicationUtils() {
    }

    public static void setApplication(Application application) {
        sApplication = application;
    }

    public static Application getApplication() {
        if (sApplication == null) {
            sApplication = getApplicationByReflect();
        }
        return sApplication;
    }

    public static Context getContext() {
        return getApplication();
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

    /**
     * 判断当前是否为调试模式
     * @param context 上下文
     * @return true or false
     */
    public static boolean isDebugMode(Context context) {
        if (sDebugMode == null) {
            sDebugMode = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
        return sDebugMode;
    }

    /**
     * 设置当前是否为调试模式
     * @param debugMode true or false
     */
    public static void setDebugMode(boolean debugMode) {
        sDebugMode = debugMode;
    }
}
