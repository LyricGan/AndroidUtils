package com.lyric.android.app.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.utils.FileUtils;
import com.lyric.android.app.utils.LogUtils;
import com.lyric.android.app.network.NetworkCallback;
import com.lyric.android.app.network.NetworkManager;
import com.lyric.android.app.utils.DisplayUtils;
import com.lyric.android.app.utils.ImageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lyricgan
 * @date 2018/1/2 17:35
 */
public class Test {
    private static final String TAG = Test.class.getName();

    public static void requestNews(Object tag, NetworkCallback callback) {
        // 类型,top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
        final String TEST_URL = "http://v.juhe.cn/toutiao/index";
        Map<String, String> params = new HashMap<>();
        params.put("device", "android");
        params.put("key", "f909a4cf8e87f8553c95f6d4989d1559");
        params.put("type", "top");
        NetworkManager.getInstance().get(TEST_URL, params, tag, callback);
    }

    public static void dirs(Context context) {
        StringBuilder builder = new StringBuilder("");
        File cacheDir = context.getCacheDir();
        if (cacheDir != null) {
            builder.append("cacheDir:").append(cacheDir.getPath()).append(",").append(cacheDir.length()).append("\n");
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            File codeCacheDir = context.getCodeCacheDir();
            if (codeCacheDir != null) {
                builder.append("codeCacheDir:").append(codeCacheDir.getPath()).append(",").append(codeCacheDir.length()).append("\n");
            }
        }
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null) {
            builder.append("externalCacheDir:").append(externalCacheDir.getPath()).append(",").append(externalCacheDir.length()).append("\n");
        }
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS);
        if (externalFilesDir != null) {
            builder.append("externalFilesDir:").append(externalFilesDir.getPath()).append(",").append(externalFilesDir.length()).append("\n");
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            File dataDir = context.getDataDir();
            if (dataDir != null) {
                builder.append("externalFilesDir:").append(dataDir.getPath()).append(",").append(dataDir.length()).append("\n");
            }
        }
        File obbDir = context.getObbDir();
        if (obbDir != null) {
            builder.append("obbDir:").append(obbDir.getPath()).append(",").append(obbDir.length()).append("\n");
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            File noBackupFilesDir = context.getNoBackupFilesDir();
            if (noBackupFilesDir != null) {
                builder.append("noBackupFilesDir:").append(noBackupFilesDir.getPath()).append(",").append(noBackupFilesDir.length()).append("\n");
            }
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
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (externalStoragePublicDirectory != null) {
            builder.append("externalStoragePublicDirectory:").append(externalStoragePublicDirectory.getPath())
                    .append(",").append(externalStoragePublicDirectory.length()).append("\n");
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

        DisplayUtils.log(context);
    }
}
