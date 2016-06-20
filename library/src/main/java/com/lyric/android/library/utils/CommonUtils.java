package com.lyric.android.library.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 常用工具类，抽象类
 * 
 * @author ganyu
 * @created 2015-4-21
 * 
 */
public abstract class CommonUtils {
	
	/**
	 * 获取应用名称
	 * @param context 上下文对象
	 * @return
	 */
	public static String getAppName(Context context) {
		PackageManager pm = context.getPackageManager();
		return context.getApplicationInfo().loadLabel(pm).toString();
	}
	
	/**
	 * 获取应用版本号
	 * @param context 上下文对象
	 * @return 版本号
	 */
	public static String getAppVersion(Context context) {
		String versionName = null;
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo;
		try {
			// 获取当前类的包名，0代表获取版本信息
			packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
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
		return VERSION.SDK_INT;
	}
	
	/**
	 * 判断SD卡是否存在
	 * @return
	 */
	public static boolean isSdCardExists() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
	
	/**
	 * 获取磁盘缓存目录
	 * @param context 上下文对象
	 * @param dirName 缓存目录名称
	 * @return
	 */
    public static String getDiskCacheDir(Context context, String dirName) {
        String cachePath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.getPath();
            }
        }
        if (TextUtils.isEmpty(cachePath)) {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.exists()) {
                cachePath = cacheDir.getPath();
            }
        }
        return cachePath + File.separator + dirName;
    }

    /**
	 * 判断字符串是否为数字
	 * @param str 字符串
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * 判断字符串是否为浮点数
	 * @param str 字符串
	 * @return
	 */
	public static boolean isFloat(String str) {
		Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
		return pattern.matcher(str).matches();
	}
	
	/**
	 * 获取配置文件标签中的值
	 * @param context 上下文对象
	 * @param metaKey 标签中的键
	 * @return 配置文件标签中的值
	 */
	public static String getMetaValue(Context context, String metaKey) {
		if (context == null || TextUtils.isEmpty(metaKey)) {
			return null;
		}
        String metaValue = null;
        try {
        	PackageManager packageManager = context.getPackageManager();
            ApplicationInfo appInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            metaValue = appInfo.metaData.getString(metaKey);
        } catch (NameNotFoundException e) {
        	e.printStackTrace();
        }
        return metaValue;
    }
	
	/**
	 * 判断服务是否已启动
	 * @param context
	 * @param serviceName
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String serviceName) {
		if (serviceName == null) {
			return false;
		}
		final int maxRunningServiceNum = 200;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServiceList = activityManager.getRunningServices(maxRunningServiceNum);
		for (RunningServiceInfo runningServiceInfo : runningServiceList) {
			if (runningServiceInfo.service.getClassName().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断应用是否启动
	 * @param context
	 * @return
	 */
	public static boolean isAppRunning(Context context) {
		final int maxRunningTaskNum = 200;
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskList = activityManager.getRunningTasks(maxRunningTaskNum);
		String packageName = context.getPackageName();
		for (RunningTaskInfo runningTaskInfo : runningTaskList) {
			if (runningTaskInfo.topActivity.getPackageName().equals(packageName)) {
				return true;
			}
		}
		return false;
	}
	
	/** 
     * 检查手机上是否安装了指定的软件 
     * @param context 
     * @param packageName 应用包名
     * @return 
     */  
	public static boolean isAppInstalled(Context context, String packageName) {
		PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
		List<String> packageNameList = new ArrayList<String>();
		if (packageInfoList != null) {
			for (int i = 0; i < packageInfoList.size(); i++) {
				String packName = packageInfoList.get(i).packageName;
				packageNameList.add(packName);
			}
		}
		// 判断包名列表中是否有目标程序的包名
		return packageNameList.contains(packageName);
	}
	
	/**
	 * 检查是否有新的版本
	 * @param appVersion 当前应用版本
	 * @param newVersion 服务器最新版本
	 * @param isForce 是否强制更新
	 * @return 0表示没有更新，1表示有更新，2表示强制更新（应用出现严重问题）
	 */
	public static int checkUpdate(String appVersion, String newVersion, int isForce) {
		if (!TextUtils.isEmpty(appVersion) && !TextUtils.isEmpty(newVersion)) {
			if (appVersion.equals(newVersion) || "1.0.0".equals(newVersion)) {
				return 0;
			}
			try {
				appVersion = appVersion.replace(".", "");
				newVersion = newVersion.replace(".", "");
				int appVersionInt = Integer.parseInt(appVersion);
				int newVersionInt = Integer.parseInt(newVersion);
				if (newVersionInt > appVersionInt) {
					if (isForce == 0) {
						return 1;
					} else {
						return 2;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

    /**
     * 判断是否在主线程
     * @return boolean
     */
    public static boolean isMainThread() {
        return (Looper.myLooper() == Looper.getMainLooper());
    }
}
