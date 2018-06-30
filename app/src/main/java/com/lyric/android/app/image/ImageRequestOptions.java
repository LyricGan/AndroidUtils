package com.lyric.android.app.image;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * image request options
 *
 * @author lyricgan
 */
public class ImageRequestOptions extends RequestOptions {

    public static RequestOptions getRequestOptions(int placeholderId) {
        return getRequestOptions(placeholderId, placeholderId, placeholderId);
    }

    public static RequestOptions getRequestOptions(int placeholderId, int errorId, int fallbackId) {
        return getDefaultRequestOptions().placeholder(placeholderId).error(errorId).fallback(fallbackId);
    }

    public static RequestOptions getDefaultRequestOptions() {
        return getRequestOptions(DiskCacheStrategy.AUTOMATIC, false, DecodeFormat.PREFER_RGB_565);
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
    public static RequestOptions getRequestOptions(DiskCacheStrategy diskCacheStrategy, boolean skipMemoryCache, DecodeFormat decodeFormat) {
        return RequestOptions.diskCacheStrategyOf(diskCacheStrategy)
                .skipMemoryCache(skipMemoryCache)
                .format(decodeFormat);
    }
}
