package com.lyric.image;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * 自定义图片加载组件
 * @author lyricgan
 * @date 17/12/24 下午8:29
 */
@GlideModule
public class ImageModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .format(DecodeFormat.PREFER_RGB_565);
        builder.setDefaultRequestOptions(requestOptions)
                .setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
    }
}
