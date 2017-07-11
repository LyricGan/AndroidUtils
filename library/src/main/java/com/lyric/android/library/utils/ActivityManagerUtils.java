package com.lyric.android.library.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * @author lyric
 * @description
 * @time 2016/6/24 11:08
 */
public class ActivityManagerUtils {

    private ActivityManagerUtils() {
    }

    /**
     * 判断服务是否已启动
     * @param context Context
     * @param serviceName the service name
     * @return true of false
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        CheckUtils.checkContext(context);
        if (TextUtils.isEmpty(serviceName)) {
            return false;
        }
        final int maxRunningServiceNum = 200;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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
        CheckUtils.checkContext(context);
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        final int maxRunningTaskNum = 200;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskList = activityManager.getRunningTasks(maxRunningTaskNum);
        for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskList) {
            if (runningTaskInfo.topActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
