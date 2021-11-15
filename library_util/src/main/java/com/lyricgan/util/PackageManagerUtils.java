package com.lyricgan.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import java.util.List;

/**
 * 包管理工具类
 * @author Lyric Gan
 */
public class PackageManagerUtils {

    private PackageManagerUtils() {
    }

    /**
     * 获取应用名称
     * @param context 上下文
     * @return 应用名称
     */
    public static String getAppName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return context.getApplicationInfo().loadLabel(packageManager).toString();
    }

    /**
     * 获取版本号
     * @param context 上下文
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取版本名称
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取配置文件标签中的值
     * @param context 上下文
     * @param metaKey 标签中的键
     * @return 配置文件标签中的值
     */
    public static String getMetaData(Context context, String metaKey) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo.metaData.getString(metaKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备安装的应用程序列表
     * @param context 上下文
     * @param flag 选项标识
     * @return 安装的应用程序列表
     */
    public static List<ApplicationInfo> getInstalledApplications(Context context, int flag) {
        return context.getPackageManager().getInstalledApplications(flag);
    }

    /**
     * 获取应用签名
     * @param context 上下文
     * @param packageName 应用包名
     * @return 应用签名
     */
    public static Signature getSignature(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
        for (PackageInfo packageInfo : packageInfoList) {
            if (packageName.equals(packageInfo.packageName)) {
                return packageInfo.signatures[0];
            }
        }
        return null;
    }

    /**
     * 判断设备上是否安装了指定的应用
     * @param context 上下文
     * @param packageName 应用包名
     * @return true or false
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return (packageInfo != null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
