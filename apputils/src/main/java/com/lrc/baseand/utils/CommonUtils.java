package com.lrc.baseand.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

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
     * 判断是否支持断点续传
     * @param response HttpResponse
     * @return
     */
    public static boolean isSupportRange(HttpResponse response) {
        if (response == null) {
        	return false;
        }
        Header header = response.getFirstHeader("Accept-Ranges");
        if (header != null) {
            return "bytes".equals(header.getValue());
        }
        header = response.getFirstHeader("Content-Range");
        if (header != null) {
            String value = header.getValue();
            return value != null && value.startsWith("bytes");
        }
        return false;
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
	 * 判断字符串是否为手机号码
	 * @param str 字符串
	 * @return 
	 */
	public static boolean isMobile(String str) {
		String patternStr = "^13[0-9]{1}[0-9]{8}$|15[012356789]{1}[0-9]{8}$|18[012356789]{1}[0-9]{8}$|14[57]{1}[0-9]{8}$|17[07]{1}[0-9]{8}$";
		Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
	}
	
	/**
	 * 判断字符串是否为邮箱格式
	 * @param str 字符串
	 * @return
	 */
	public static boolean isEmail(String str) {
		String patternStr = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/**
	 * 获取字符串长度
	 * @param value
	 * @return
	 */
	public static int getCharLength(String value) {
		if (TextUtils.isEmpty(value)) {
			return 0;
		}
		int valueLength = 0;
		String chineseChar = "[\u0391-\uFFE5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < value.length(); i++) {
			// 获取一个字符
			String temp = value.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chineseChar)) {
				// 中文字符长度为2
				valueLength += 2;
			} else {
				// 非中文字符长度为1
				valueLength += 1;
			}
		}
		return valueLength;
	}
	
	/**
	 * 判断是否包含中文
	 * @param value
	 * @return
	 */
	public static boolean hasChineseChar(String value) {
		if (TextUtils.isEmpty(value)) {
			return false;
		}
		String chineseChar = "[\u0391-\uFFE5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chineseChar)) {
				return true;
			}
		}
		return false;
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
	 * 将应用语言设置为简体中文
	 * 
	 * @param context
	 */
	public static void setLanguageChinese(Context context) {
		Locale locale = new Locale("zh");
		Locale.setDefault(locale);
		Configuration config = context.getResources().getConfiguration();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		config.locale = Locale.SIMPLIFIED_CHINESE;
		context.getResources().updateConfiguration(config, metrics);
	}

	/**
	 * 将应用语言设置为英文
	 * 
	 * @param context
	 */
	public static void setLanguageEnglish(Context context) {
		Locale locale = new Locale("english");
		Locale.setDefault(locale);
		Configuration config = context.getResources().getConfiguration();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		config.locale = Locale.ENGLISH;
		context.getResources().updateConfiguration(config, metrics);
	}

	/**
	 * 获得应用当前语言
	 * 
	 * @param context
	 * @return cn--中文 en-英文
	 */
	public static String getCurrentLanguage(Context context) {
		Configuration config = context.getResources().getConfiguration();
		return config.locale.toString().equals(Locale.SIMPLIFIED_CHINESE.toString()) ? "cn" : "en";
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
	 * 以JSON格式返回mac地址和设备ID
	 * @param context
	 * @return
	 */
	public static String getDeviceInfo(Context context) {
		try {
			JSONObject json = new JSONObject();
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String device_id = tm.getDeviceId();
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);
			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}
			if (TextUtils.isEmpty(device_id)) {
				device_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
			}
			json.put("device_id", device_id);
			
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
