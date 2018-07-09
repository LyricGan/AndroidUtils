package com.lyric.android.app.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.lyric.android.app.AndroidApplication;

import java.io.File;
import java.util.List;

/**
 * @author lyricgan
 */
public class Test {
    private static final String TAG = "Test";

    public static void showLogs(Context context) {
        StringBuilder builder = new StringBuilder("");
        File cacheDir = context.getCacheDir();
        if (cacheDir != null) {
            builder.append("cacheDir:").append(cacheDir.getPath()).append(",").append(cacheDir.length()).append("\n");
        }
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null) {
            builder.append("externalCacheDir:").append(externalCacheDir.getPath()).append(",").append(externalCacheDir.length()).append("\n");
        }

        String packageCodePath = context.getPackageCodePath();
        builder.append("packageCodePath:").append(packageCodePath).append("\n");
        String packageResourcePath = context.getPackageResourcePath();
        builder.append("packageResourcePath:").append(packageResourcePath).append("\n");

        File dataDirectory = Environment.getDataDirectory();
        if (dataDirectory != null) {
            builder.append("dataDirectory:").append(dataDirectory.getPath()).append(",").append(dataDirectory.length()).append("\n");
        }
        File rootDirectory = Environment.getRootDirectory();
        if (rootDirectory != null) {
            builder.append("rootDirectory:").append(rootDirectory.getPath()).append(",").append(rootDirectory.length()).append("\n");
        }
        File downloadCacheDirectory = Environment.getDownloadCacheDirectory();
        if (downloadCacheDirectory != null) {
            builder.append("downloadCacheDirectory:").append(downloadCacheDirectory.getPath()).append(",").append(downloadCacheDirectory.length()).append("\n");
        }
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory != null) {
            builder.append("externalStorageDirectory:").append(externalStorageDirectory.getPath()).append(",").append(externalStorageDirectory.length()).append("\n");
        }

        LogUtils.d(TAG, builder.toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> externalImagePaths = FileUtils.queryExternalImages(AndroidApplication.getContext());
                if (externalImagePaths == null || externalImagePaths.isEmpty()) {
                    return;
                }
                StringBuilder externalBuilder = new StringBuilder("");
                for (int i = 0; i < externalImagePaths.size(); i++) {
                    String imagePath = externalImagePaths.get(i);
                    externalBuilder.append("external imagePath:").append(imagePath).append("\n");

                    Bitmap bitmap = ImageUtils.decodeBitmap(imagePath, 0, 0);
                    if (bitmap != null) {
                        externalBuilder.append("external bitmap:")
                                .append(bitmap.getByteCount())
                                .append(",")
                                .append(bitmap.getWidth())
                                .append("x")
                                .append(bitmap.getHeight())
                                .append("\n");
                    } else {
                        externalBuilder.append("external bitmap is null").append("\n");
                    }
                }
                LogUtils.d(TAG, externalBuilder.toString());

                List<String> internalImagePaths = FileUtils.queryInternalImages(AndroidApplication.getContext());
                if (internalImagePaths == null || internalImagePaths.isEmpty()) {
                    return;
                }
                StringBuilder internalBuilder = new StringBuilder("");
                for (int i = 0; i < internalImagePaths.size(); i++) {
                    internalBuilder.append("internal imagePath:").append(internalImagePaths.get(i)).append("\n");
                }
                LogUtils.d(TAG, internalBuilder.toString());

                List<File> externalFileDirs = FileUtils.queryImageDirs(externalImagePaths);
                StringBuilder externalFileDirsBuilder = new StringBuilder("");
                for (int i = 0; i < externalFileDirs.size(); i++) {
                    externalFileDirsBuilder.append("external filePath:").append(externalFileDirs.get(i).getPath()).append("\n");
                }
                LogUtils.d(TAG, externalFileDirsBuilder.toString());

                List<File> internalFileDirs = FileUtils.queryImageDirs(internalImagePaths);
                StringBuilder internalFileDirsBuilder = new StringBuilder("");
                for (int i = 0; i < internalFileDirs.size(); i++) {
                    internalFileDirsBuilder.append("internal filePath:").append(internalFileDirs.get(i).getPath()).append("\n");
                }
                LogUtils.d(TAG, internalFileDirsBuilder.toString());
            }
        }).start();

        StringBuilder displayBuilder = new StringBuilder();
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        displayBuilder.append("density=").append(displayMetrics.density).append("\n")
                .append("densityDpi=").append(displayMetrics.densityDpi).append("\n")
                .append("scaledDensity=").append(displayMetrics.scaledDensity).append("\n")
                .append("screenWidth=").append(displayMetrics.widthPixels).append("\n")
                .append("screenHeight=").append(displayMetrics.heightPixels).append("\n")
                .append("xdpi=").append(displayMetrics.xdpi).append("\n")
                .append("ydpi=").append(displayMetrics.ydpi).append("\n");

        LogUtils.d(TAG, displayBuilder.toString());
    }
}
