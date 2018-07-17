package com.lyric.android.app.common;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * custom image loader component
 *
 * @author lyricgan
 */
@GlideModule
public class ImageModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        RequestOptions requestOptions = getRequestOptions(DiskCacheStrategy.AUTOMATIC, true, DecodeFormat.PREFER_RGB_565);
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setBitmapPoolScreens(3)
                .build();
        builder.setDefaultRequestOptions(requestOptions)
                .setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()))
                .setBitmapPool(new LruBitmapPool(calculator.getBitmapPoolSize()))
                .setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context))
                .setLogLevel(Log.DEBUG);
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    /**
     * 获取图片请求参数配置
     * @param diskCacheStrategy 磁盘缓存策略
     * @param skipMemoryCache 是否启用内存缓存
     * @return 图片请求参数配置
     *
     * @see DiskCacheStrategy
     * @see DiskCacheStrategy#NONE 表示不缓存任何内容
     * @see DiskCacheStrategy#RESOURCE 表示只缓存转换过后的图片
     * @see DiskCacheStrategy#DATA 表示只缓存原始图片
     * @see DiskCacheStrategy#ALL 表示既缓存原始图片，也缓存转换过后的图片
     * @see DiskCacheStrategy#AUTOMATIC 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）
     */
    public RequestOptions getRequestOptions(DiskCacheStrategy diskCacheStrategy, boolean skipMemoryCache, DecodeFormat decodeFormat) {
        return RequestOptions.diskCacheStrategyOf(diskCacheStrategy)
                .skipMemoryCache(skipMemoryCache)
                .format(decodeFormat);
    }
}
