package com.lyric.android.app.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.lyric.android.app.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/7 14:04
 */
public class ImageLoaderUtils {

    public static void initialize(Context context) {
        // 初始化图片加载类
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.merch_cover_default)
                .showImageOnLoading(R.mipmap.merch_cover_default)
                .showImageOnFail(R.mipmap.merch_cover_default)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        // 设置图片文件缓存路径
        String imageDirectory = "/Android/data/" + context.getPackageName() + "/cache/image_loader_cache";
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, imageDirectory);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new WeakMemoryCache())
                .discCache(new UnlimitedDiscCache(cacheDir))
                .defaultDisplayImageOptions(defaultOptions)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
