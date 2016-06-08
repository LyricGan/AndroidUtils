package com.lyric.android.library.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

/**
 * 文件工具类
 *
 * @author ganyu
 * @version 2015-9-14
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * 删除缓存文件
     * @param context Context
     */
    public static void deleteCacheFolder(Context context) {
        clearCacheFolder(context.getCacheDir(), System.currentTimeMillis());
    }

    /**
     * 删除缓存文件
     * @param dir          文件目录
     * @param lastModified 时间戳
     * @return 被删除文件数量
     */
    public static int clearCacheFolder(File dir, long lastModified) {
        int deletedFileCount = 0;
        if (dir != null && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    deletedFileCount += clearCacheFolder(file, lastModified);
                }
                if (file.lastModified() < lastModified) {
                    if (file.delete()) {
                        deletedFileCount++;
                    }
                }
            }
        }
        return deletedFileCount;
    }

    /**
     * 判断文件是否存在
     * @param filePath 文件路径
     * @return boolean
     */
    public static boolean isExists(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }
}
