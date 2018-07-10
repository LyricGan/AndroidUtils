package com.lyric.android.app.utils;

import android.content.Context;

import java.io.File;

/**
 * @author lyricgan
 */
public class Test {
    private static final String TAG = Test.class.getSimpleName();

    public static void showLogs(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");

        File cacheDir = FileUtils.getCacheDir(context);
        if (cacheDir != null) {
            builder.append("file cacheDir:").append(cacheDir.getPath()).append(",").append(cacheDir.length()).append("\n");
        }
        File rootDirectory = FileUtils.getRootDirectory();
        if (rootDirectory != null) {
            builder.append("file rootDirectory:").append(rootDirectory.getPath()).append(",").append(rootDirectory.length()).append("\n");
        }
        File externalStorageDirectory = FileUtils.getExternalStorageDirectory();
        if (externalStorageDirectory != null) {
            builder.append("file externalStorageDirectory:").append(externalStorageDirectory.getPath()).append(",").append(externalStorageDirectory.length()).append("\n");
        }
        File dataDirectory = FileUtils.getDataDirectory();
        if (dataDirectory != null) {
            builder.append("file dataDirectory:").append(dataDirectory.getPath()).append(",").append(dataDirectory.length()).append("\n");
        }
        File downloadCacheDirectory = FileUtils.getDownloadCacheDirectory();
        if (downloadCacheDirectory != null) {
            builder.append("file downloadCacheDirectory:").append(downloadCacheDirectory.getPath()).append(",").append(downloadCacheDirectory.length()).append("\n");
        }
        String packageCodePath = context.getPackageCodePath();
        builder.append("packageCodePath:").append(packageCodePath).append("\n");

        LogUtils.d(TAG, builder.toString());

        LogUtils.d(TAG, DisplayUtils.toDisplayString());
    }
}
