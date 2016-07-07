package com.lyric.android.app.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.stetho.Stetho;
import com.lyric.android.app.R;
import com.lyric.android.app.fresco.ImageHelper;
import com.lyric.android.library.utils.LogUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;

/**
 * extends Application {@link Application}
 * 
 * @author ganyu
 * 
 */
public class BaseApplication extends Application {
    private static BaseApplication mContext;
    private static RefWatcher mRefWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
        mContext = this;

        initImageLoader(this);
        LogUtils.setDebug(Constants.DEBUG);
        initRefWatcher(Constants.LEAK_DEBUG);
        initializeStetho(Constants.DEBUG);
        ImageHelper.getInstance().initialize(this);
	}

	public static BaseApplication getContext() {
		return mContext;
	}

    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    private void initRefWatcher(boolean isDebug) {
        if (isDebug) {
            mRefWatcher = LeakCanary.install(this);
        } else {
            mRefWatcher = RefWatcher.DISABLED;
        }
    }

    private void initImageLoader(Context context) {
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

    private void initializeStetho(boolean isDebug) {
        if (!isDebug) {
            return;
        }
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }
}
