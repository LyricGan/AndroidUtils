package com.lyricgan.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
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
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getInstalledApplications(flag);
    }

    /**
     * 获取应用签名信息
     * @param context 上下文
     * @param packageName 应用包名
     * @return 应用签名字符串
     */
    public static String getSignatureInfo(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iterator = packageInfoList.iterator();
        Signature[] signatureArray = null;
        while (iterator.hasNext()) {
            PackageInfo packageInfo = iterator.next();
            // 判断包名是否相同
            if (packageName.equals(packageInfo.packageName)) {
                signatureArray = packageInfo.signatures;
            }
        }
        // 判断签名数组是否为空
        if (signatureArray != null) {
            StringBuilder builder = new StringBuilder();
            for (Signature itemSignature : signatureArray) {
                builder.append(itemSignature);
            }
            return builder.toString();
        }
        return null;
    }

    public static String getApkSignMd5(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES);
            } else {
                packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            }
            Signature[] signatures = packageInfo.signatures;
            byte[] signBytes = signatures[0].toByteArray();
            return Md5Utils.md5Encode(signBytes);
        } catch (Exception e) {
            e.printStackTrace();
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
        PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        List<String> packageNameList = new ArrayList<>();
        for (int i = 0; i < packageInfoList.size(); i++) {
            String packName = packageInfoList.get(i).packageName;
            packageNameList.add(packName);
        }
        // 判断包名列表中是否有目标程序的包名
        return packageNameList.contains(packageName);
    }

    public static boolean isAppInstalled2(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            return (packageInfo != null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
