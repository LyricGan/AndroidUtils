package com.lyricgan.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 文件工具类
 * <br/> 内存：
 * <br/> Context.getFilesDir()方法用于获取/data/data/<application package>/files目录
 * <br/> Context.getCacheDir()方法用于获取/data/data/<application package>/cache目录
 * <br/>
 * <br/> SD卡外存：
 * <br/> Context.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
 * <br/> Context.getExternalCacheDir()方法可以获取到 SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
 * <br/>
 * <br/> 如果需要建立隐藏文件和文件夹来避免用户删除：名称以"."开头来命名
 * @author Lyric Gan
 */
public class FileUtils {

    private FileUtils() {
    }

    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean isSDCardAvailable() {
        return isSDCardExist() && !Environment.isExternalStorageRemovable();
    }

    /**
     * 获取应用缓存目录，SD卡可用返回SD卡缓存目录，否则返回应用缓存目录
     */
    public static String getAppCacheDir(Context context) {
        File file;
        if (isSDCardAvailable()) {
            file = context.getExternalCacheDir();
        } else {
            file = context.getCacheDir();
        }
        if (file != null) {
            return file.getPath();
        }
        return null;
    }

    public static String getAppCacheDir(Context context, String fileName) {
        File file;
        String cacheDir = getAppCacheDir(context);
        if (!TextUtils.isEmpty(cacheDir)) {
            file = new File(cacheDir, fileName);
        } else {
            file = new File(fileName);
        }
        if (file.exists() && file.isDirectory()) {
            return file.getPath();
        }
        try {
            if (file.mkdirs()) {
                return file.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用文件目录，SD卡可用返回SD卡文件目录，否则返回应用文件目录
     */
    public static String getAppFilesDir(Context context, String type) {
        File file;
        if (isSDCardAvailable()) {
            file = context.getExternalFilesDir(type);
        } else {
            file = context.getFilesDir();
        }
        if (file != null) {
            return file.getPath();
        }
        return null;
    }

    public static String getAppFilesDir(Context context, String type, String fileName) {
        File file;
        String cacheDir = getAppFilesDir(context, type);
        if (!TextUtils.isEmpty(cacheDir)) {
            file = new File(cacheDir, fileName);
        } else {
            file = new File(fileName);
        }
        if (file.exists() && file.isDirectory()) {
            return file.getPath();
        }
        try {
            if (file.mkdirs()) {
                return file.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAppPictureFilePath(Context context, String fileName) {
        return getAppFilePath(context, Environment.DIRECTORY_PICTURES, fileName);
    }

    public static String getAppDownloadFilePath(Context context, String fileName) {
        return getAppFilePath(context, Environment.DIRECTORY_DOWNLOADS, fileName);
    }

    public static String getAppFilePath(Context context, String type, String fileName) {
        File file;
        String cacheDir = getAppFilesDir(context, type);
        if (!TextUtils.isEmpty(cacheDir)) {
            file = new File(cacheDir, fileName);
        } else {
            file = new File(fileName);
        }
        if (file.exists() && file.isFile()) {
            return file.getPath();
        }
        try {
            if (file.createNewFile()) {
                return file.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getByteArray(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        RandomAccessFile accessFile = null;
        byte[] data = null;
        try {
            accessFile = new RandomAccessFile(file, "r");
            long fileLength = accessFile.length();
            if (fileLength > Integer.MAX_VALUE) {
                return null;
            }
            data = new byte[(int) fileLength];
            accessFile.readFully(data);

            return data;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtils.close(accessFile);
        }
        return data;
    }

    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    public static boolean deleteFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteFile(File file) {
        if (file != null && file.exists()) {
            return deleteFile(file.getPath());
        }
        return false;
    }

    public static long getFileSize(String path) {
        long size = 0;
        try {
            File file = new File(path);
            if (!file.exists()) {
                return 0;
            }
            if (!file.isDirectory()) {
                return file.length();
            }
            File[] listFiles = file.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                return 0;
            }
            for (File item : listFiles) {
                if (item.isDirectory()) {
                    size += getFileSize(item.getPath());
                } else {
                    size += item.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String toFileString(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");

        File filesDir = context.getFilesDir();
        if (filesDir != null) {
            builder.append("filesDir:").append(filesDir.getPath()).append(",").append(filesDir.length()).append("\n");
        }
        File cacheDir = context.getCacheDir();
        if (cacheDir != null) {
            builder.append("cacheDir:").append(cacheDir.getPath()).append(",").append(cacheDir.length()).append("\n");
        }
        builder.append("packageCodePath:").append(context.getPackageCodePath()).append("\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.append("codeCacheDir:").append(context.getCodeCacheDir()).append("\n");
        }

        File rootDirectory = Environment.getRootDirectory();
        builder.append("rootDirectory:").append(rootDirectory.getPath()).append(",").append(rootDirectory.length()).append("\n");
        File dataDirectory = Environment.getDataDirectory();
        if (dataDirectory != null) {
            builder.append("dataDirectory:").append(dataDirectory.getPath()).append(",").append(dataDirectory.length()).append("\n");
        }
        File downloadCacheDirectory = Environment.getDownloadCacheDirectory();
        if (downloadCacheDirectory != null) {
            builder.append("downloadCacheDirectory:").append(downloadCacheDirectory.getPath()).append(",").append(downloadCacheDirectory.length()).append("\n");
        }
        String externalStorageState = Environment.getExternalStorageState();
        builder.append("externalStorageState:").append(externalStorageState).append("\n");
        if (TextUtils.equals(externalStorageState, Environment.MEDIA_MOUNTED)) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if (externalStorageDirectory != null) {
                builder.append("externalStorageDirectory:").append(externalStorageDirectory.getPath()).append(",").append(externalStorageDirectory.length()).append("\n");
            }
            File externalFileDir = context.getExternalFilesDir(null);
            if (externalFileDir != null) {
                builder.append("externalFileDir:").append(externalFileDir.getPath()).append(",").append(externalFileDir.length()).append("\n");
            }
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                builder.append("externalCacheDir:").append(externalCacheDir.getPath()).append(",").append(externalCacheDir.length()).append("\n");
            }
        }
        return builder.toString();
    }
}