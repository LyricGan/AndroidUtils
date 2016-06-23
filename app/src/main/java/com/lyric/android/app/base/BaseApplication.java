package com.lyric.android.app.base;

import android.app.Application;
import android.graphics.Bitmap;

import com.lyric.android.app.R;
import com.lyric.android.app.constants.Constants;
import com.lyric.android.library.utils.LogUtils;
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
 * extends Application {@link Application}
 * 
 * @author ganyu
 * 
 */
public class BaseApplication extends Application {
    private static BaseApplication sApplication;
//    private static RefWatcher mRefWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
        sApplication = this;

		LogUtils.setDebug(Constants.DEBUG);

//        if (Constants.LEAK_DEBUG) {
//            mRefWatcher = LeakCanary.install(this);
//        } else {
//            mRefWatcher = RefWatcher.DISABLED;
//        }

        initImageLoader();
	}

	public static BaseApplication getContext() {
		return sApplication;
	}

//    public static RefWatcher getRefWatcher() {
//        return mRefWatcher;
//    }

    private void initImageLoader() {
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
        String imageDirectory = "/Android/data/" + getApplicationContext().getPackageName() + "/cache/image_loader_cache";
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), imageDirectory);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new WeakMemoryCache())
                .discCache(new UnlimitedDiscCache(cacheDir))
                .defaultDisplayImageOptions(defaultOptions)
//			.writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
