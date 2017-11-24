package com.lyric.android.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * 工具类，封装部分常用方法
 * @author lyricgan
 * @time 16/12/9
 */
public class Utils {

    /**
     * 文件大小类型，单位为B
     */
    public static final int SIZE_TYPE_B = 1;
    /**
     * 文件大小类型，单位为KB
     */
    public static final int SIZE_TYPE_KB = 2;
    /**
     * 文件大小类型，单位为MB
     */
    public static final int SIZE_TYPE_MB = 3;
    /**
     * 文件大小类型，单位为GB
     */
    public static final int SIZE_TYPE_GB = 4;

    /**
     * 获取指定文件大小
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        return getFileSize(file);
    }

    /**
     * 获取指定文件夹大小
     * @param filePath
     * @return
     */
    public static long getFilesSize(String filePath) {
        File file = new File(filePath);
        long size;
        if (file.isDirectory()) {
            size = getFilesSize(file);
        } else {
            size = getFileSize(file);
        }
        return size;
    }

    /**
     * 获取指定文件大小
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 获取指定文件夹大小
     * @param file
     * @return
     */
    public static long getFilesSize(File file) {
        long size = 0;
        File[] fileArray = file.listFiles();
        for (int i = 0; i < fileArray.length; i++) {
            if (fileArray[i].isDirectory()) {
                size = size + getFilesSize(fileArray[i]);
            } else {
                size = size + getFileSize(fileArray[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileSize
     * @return
     */
    public static String formatFileSize(long fileSize) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String fileSizeString;
        String wrongSize = "0B";
        if (fileSize == 0) {
            return wrongSize;
        }
        if (fileSize < 1024) {
            fileSizeString = decimalFormat.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = decimalFormat.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = decimalFormat.format((double) fileSize / 1048576) + "MB";
        } else {
            fileSizeString = decimalFormat.format((double) fileSize / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileSize
     * @param sizeType
     * @return
     */
    public static double formatFileSize(long fileSize, int sizeType) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZE_TYPE_B:
                fileSizeLong = Double.valueOf(decimalFormat.format((double) fileSize));
                break;
            case SIZE_TYPE_KB:
                fileSizeLong = Double.valueOf(decimalFormat.format((double) fileSize / 1024));
                break;
            case SIZE_TYPE_MB:
                fileSizeLong = Double.valueOf(decimalFormat.format((double) fileSize / 1048576));
                break;
            case SIZE_TYPE_GB:
                fileSizeLong = Double.valueOf(decimalFormat.format((double) fileSize / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
}
