package com.lyric.android.library.utils;

import android.os.Build;
import android.text.TextUtils;

public class BuildVersionUtils {

    private BuildVersionUtils() {
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasHoneycombMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }
    
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
    
    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
    
    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
    
    public static boolean hasLollipop(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasMarshmallow(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 获取设备名称
     * @return 手机设备名称
     */
    public static String getDeviceName() {
        return Build.DEVICE;
    }

    /**
     * 获取当前系统的android版本号
     * @return 系统的版本号
     */
    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 检查是否有新的版本
     * @param appVersion 当前应用版本
     * @param newVersion 服务器最新版本
     * @param isForce 是否强制更新
     * @return 0表示没有更新，1表示有更新，2表示强制更新（应用出现严重问题）
     */
    public static int checkUpdate(String appVersion, String newVersion, boolean isForce) {
        int update = 0;
        if (TextUtils.isEmpty(appVersion) || TextUtils.isEmpty(newVersion)) {
            return update;
        }
        if (appVersion.equals(newVersion) || "1.0.0".equals(newVersion)) {
            return update;
        }
        try {
            appVersion = appVersion.replace(".", "");
            newVersion = newVersion.replace(".", "");
            int appVersionInt = Integer.parseInt(appVersion);
            int newVersionInt = Integer.parseInt(newVersion);
            if (newVersionInt > appVersionInt) {
                update = isForce ? 2 : 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return update;
    }
}