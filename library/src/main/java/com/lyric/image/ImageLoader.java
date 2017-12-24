package com.lyric.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

/**
 * 图片加载器
 *
 * @author lyricgan
 * @date 17/12/23 下午7:33
 */
public class ImageLoader {

    private ImageLoader() {
    }

    private static final class ImageLoaderHolder {
        private static final ImageLoader IMAGE_LOADER = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return ImageLoaderHolder.IMAGE_LOADER;
    }

    public RequestOptions getRequestOptions(int placeholderId) {
        return getRequestOptions(placeholderId, placeholderId, DiskCacheStrategy.AUTOMATIC, false);
    }

    /**
     * 获取图片请求参数配置
     * @param placeholderId 默认占位图资源ID
     * @param errorId 异常占位图资源ID
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
    public RequestOptions getRequestOptions(int placeholderId, int errorId, DiskCacheStrategy diskCacheStrategy, boolean skipMemoryCache) {
        return RequestOptions.placeholderOf(placeholderId)
                .error(errorId)
                .diskCacheStrategy(diskCacheStrategy)
                .skipMemoryCache(skipMemoryCache);
    }

    public void load(Context context, Object model, ImageView view) {
        load(context, model, view, null);
    }

    public void load(Context context, Object model, ImageView view, RequestOptions requestOptions) {
        load(context, model, view, requestOptions, null);
    }

    public void load(Context context, Object model, ImageView view, RequestOptions requestOptions, RequestListener<Drawable> listener) {
        RequestManager requestManager = Glide.with(context);
        RequestBuilder<Drawable> requestBuilder = requestManager.load(model);
        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply(requestOptions);
        }
        requestBuilder = requestBuilder.listener(listener);
        requestBuilder.into(view);
    }
}
