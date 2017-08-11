package com.lyric.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

/**
 * 设备工具类
 * 
 * @author ganyu
 * 
 */
public class DeviceUtils {

    private DeviceUtils() {
    }

	/**
	 * 判断设备是否具备root权限
	 * @return boolean
	 */
	public static boolean isRoot() {
		final String[] suPathArray = { "/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/" };
		File file = null;
		try {
			for (int i = 0; i < suPathArray.length; i++) {
				String filePath = suPathArray[i] + "su";
				file = new File(filePath);
				if (file.exists()) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

    public static int getAppUid(Context context, String packageName) {
        int uid = 0;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, PackageManager.GET_ACTIVITIES);
            uid = appInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return uid;
    }

    /**
     * 获取设备ID，需要添加权限{@link android.Manifest.permission#ACCESS_WIFI_STATE}
     * @param context Context
     * @return 设备ID
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            deviceId = wifi.getConnectionInfo().getMacAddress();
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    /**
     * 将应用语言设置为简体中文
     * @param context Context
     */
    public static void setLanguageChinese(Context context) {
        String languageToLoad = "zh";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        config.locale = Locale.SIMPLIFIED_CHINESE;
        context.getResources().updateConfiguration(config, metrics);
    }

    /**
     * 将应用语言设置为英文
     * @param context Context
     */
    public static void setLanguageEnglish(Context context) {
        String languageToLoad = "english";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        config.locale = Locale.ENGLISH;
        context.getResources().updateConfiguration(config, metrics);
    }

    /**
     * 获得应用当前语言
     * @param context Context
     * @return cn--中文 en-英文
     */
    public static String getCurrentLanguage(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return config.locale.toString().equals(Locale.SIMPLIFIED_CHINESE.toString()) ? "cn" : "en";
    }
}