package com.lyric.android.app.third.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.io.File;

/**
 * @author lyricgan
 * @description 网络图片加载类，{@link ImageModule}
 * @time 2016/6/3 13:23
 */
public class ImageLoader {
    private static final String CACHE_NAME = "cache_network_images";
    private static final int DISK_CACHE_SIZE = 200 * 1024 * 1024;

    private ImageLoader() {
    }

    public static void initialize(Context context) {
        GlideBuilder builder = new GlideBuilder(context);
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, CACHE_NAME, DISK_CACHE_SIZE));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        // 暂时这样处理
        if (!Glide.isSetup()) {
            Glide.setup(builder);
        }
    }

    public static void load(Context context, String url, ImageView view) {
        Glide.with(context).load(url).into(view);
    }

    /**
     * 加载图片
     * @param view ImageView
     * @param object
     * @param placeHolderId 占位图
     * @param errorResId 错误占位图
     */
    public static void load(ImageView view, Object object, int placeHolderId, int errorResId) {
        if (view == null || object == null) {
            return;
        }
        GenericRequestBuilder builder = Glide.with(view.getContext()).load(object).crossFade();
        if (placeHolderId > 0) {
            builder = builder.placeholder(placeHolderId);
        }
        if (errorResId > 0) {
            builder = builder.error(errorResId);
        }
        builder.into(view);
    }

    public static File getCacheDir(Context context) {
        return Glide.getPhotoCacheDir(context, CACHE_NAME);
    }

    public static void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    public static Bitmap getBitmap(GlideDrawable glideDrawable) {
        if (glideDrawable instanceof GlideBitmapDrawable) {
            return ((GlideBitmapDrawable) glideDrawable).getBitmap();
        } else if (glideDrawable instanceof GifDrawable) {
            return ((GifDrawable) glideDrawable).getFirstFrame();
        }
        return null;
    }
}
