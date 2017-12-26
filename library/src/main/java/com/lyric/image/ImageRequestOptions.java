package com.lyric.image;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * 图片请求参数配置
 * @author lyricgan
 * @date 2017/12/26 14:31
 */
public class ImageRequestOptions extends RequestOptions {

    public static RequestOptions getRequestOptions(int placeholderId) {
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
    public static RequestOptions getRequestOptions(int placeholderId, int errorId, DiskCacheStrategy diskCacheStrategy, boolean skipMemoryCache) {
        return RequestOptions.placeholderOf(placeholderId)
                .error(errorId)
                .diskCacheStrategy(diskCacheStrategy)
                .skipMemoryCache(skipMemoryCache);
    }
}
