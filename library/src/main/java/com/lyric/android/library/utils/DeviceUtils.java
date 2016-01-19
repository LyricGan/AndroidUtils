package com.lyric.android.library.utils;

import java.io.File;

/**
 * 设备工具类
 * 
 * @author ganyu
 * 
 */
public class DeviceUtils {

	/**
	 * 判断设备是否具备root权限
	 * @return
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

}