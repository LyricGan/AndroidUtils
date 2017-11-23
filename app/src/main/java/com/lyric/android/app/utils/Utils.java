package com.lyric.android.app.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

/**
 * 工具类，封装部分常用方法
 * @author lyric
 * @time 16/12/9
 */
public class Utils {

    /**
     * 判断网络是否连接
     * @param context 上下文对象
     * @return 网络是否连接
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }
        return false;
    }

    /**
     * 获取应用签名信息
     *
     * @param context     上下文对象
     * @param packageName 应用包名
     * @return 应用签名信息
     */
    public static String getSignatureInfo(Context context, String packageName) {
        // 判断应用包名是否为空
        if (packageName == null) {
            return null;
        }
        Signature[] signatureArray = null;
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iterator = packageInfoList.iterator();
        String packname;
        while (iterator.hasNext()) {
            PackageInfo packageInfo = iterator.next();
            packname = packageInfo.packageName;
            // 判断包名是否相同
            if (packageName.equals(packname)) {
                signatureArray = packageInfo.signatures;
            }
        }
        // 判断签名数组是否为空
        if (signatureArray != null) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < signatureArray.length; i++) {
                buffer.append(signatureArray[i]);
            }
            return buffer.toString();
        }
        return null;
    }

    /**
     * Method for return file path of Gallery image
     *
     * @param context
     * @param uri
     * @return path of the selected image file from gallery
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

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
