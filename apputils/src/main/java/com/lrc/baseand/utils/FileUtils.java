package com.lrc.baseand.utils;

import java.io.File;

import android.content.Context;
/**
 * 文件工具类
 * 
 * @author ganyu
 * @version 2015-9-14
 */
public class FileUtils {
	
	/**
	 * 删除缓存文件
	 * @param context
	 */
	public static void clearCacheFolder(Context context) {
		clearCacheFolder(context.getCacheDir(), System.currentTimeMillis());
	}
	
	/**
	 * 删除缓存文件
	 * @param dir 文件目录
	 * @param lastModified 时间戳
	 * @return 被删除文件数量
	 */
	public static int clearCacheFolder(File dir, long lastModified) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, lastModified);
					}
					if (child.lastModified() < lastModified) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}
	
}
