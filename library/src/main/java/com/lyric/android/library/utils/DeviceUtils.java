package com.lyric.android.library.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.File;

/**
 * 设备工具类
 * 
 * @author ganyu
 * 
 */
public class DeviceUtils {

    DeviceUtils() {
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
}