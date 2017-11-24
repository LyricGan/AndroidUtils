package com.lyric.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.PowerManager;
import android.text.TextUtils;

import java.util.List;

/**
 * 常用工具类
 * @author lyricgan
 * @date 2017/11/24 11:38
 */
public class CommonUtils {

    private CommonUtils() {
    }

    /**
     * 判断服务是否已启动
     * @param context Context
     * @param serviceName the service name
     * @return true of false
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        if (TextUtils.isEmpty(serviceName)) {
            return false;
        }
        final int maxRunningServiceNum = 200;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningServiceInfo> runningServiceList = activityManager.getRunningServices(maxRunningServiceNum);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServiceList) {
            if (runningServiceInfo.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断应用是否启动
     * @param context Context
     * @param packageName the package name of app
     * @return true of false
     */
    public static boolean isAppRunning(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        final int maxRunningTaskNum = 200;
        List<ActivityManager.RunningTaskInfo> runningTaskList = activityManager.getRunningTasks(maxRunningTaskNum);
        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskList) {
            if (runningTaskInfo.topActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断应用是否处于前台，需要同时满足两个条件：1、运行的进程中有当前应用进程，2、顶部activity对应的包与当前应用包名一致<br/>
     * 需要权限android.Manifest.permission.GET_TASKS
     * @param context Context
     * @param packageName the package name of app
     * @return true or false
     */
    public static boolean isAppOnForeground(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        // 获取所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null || appProcesses.size() <= 0) {
            return false;
        }
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager == null) {
            return false;
        }
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (TextUtils.equals(appProcess.processName, packageName)) {
                // 判断是否位于后台
                boolean isBackground = (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE);
                // 判断是否为锁屏状态
                boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
                // 判断屏幕是否点亮
                boolean isScreenOff = !powerManager.isScreenOn();
                if (isBackground || isLockedState || isScreenOff) {
                    return false;
                } else {
                    List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
                    if (tasks != null && !tasks.isEmpty()) {
                        ComponentName topActivity = tasks.get(0).topActivity;
                        if (topActivity.getPackageName().equals(packageName)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 判断指定的activity是否正在显示
     * @param context Context
     * @param activityCls 指定的activity
     * @return true or false
     */
    public static boolean isActivityOnTop(Context context, Class<? extends Activity> activityCls) {
        if (!isAppOnForeground(context, context.getPackageName())) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return false;
        }
        try {
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            String className = cn.getClassName();
            if (!TextUtils.isEmpty(className) && className.equals(activityCls.getName())) {
                return true;
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
