package com.lyric.android.app.fresco;

import android.content.Context;
import android.os.Environment;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * @author lyricgan
 * @description image factory
 * @time 2016/7/6 18:20
 */
class ImageFactory {
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    private static final int MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 5;
    private static final String IMAGE_PIPELINE_CACHE_DIR_CHILD = "image_cache_dir";
    private static ImagePipelineConfig sImagePipelineConfig;

    public static ImagePipelineConfig getImagePipelineConfig(Context context) {
        if (sImagePipelineConfig == null) {
            ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context);
            configureCaches(configBuilder, context);
            sImagePipelineConfig = configBuilder.build();
        }
        return sImagePipelineConfig;
    }

    private static void configureCaches(ImagePipelineConfig.Builder configBuilder, Context context) {
        Supplier<MemoryCacheParams> bitmapMemoryCacheParamsSupplier = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return new MemoryCacheParams(
                        MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                        Integer.MAX_VALUE,                     // Max entries in the cache
                        MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                        Integer.MAX_VALUE,                     // Max length of eviction queue
                        Integer.MAX_VALUE);                    // Max cache entry size;
            }
        };
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(Environment.getExternalStorageDirectory())
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR_CHILD)
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                .build();
        configBuilder
                .setBitmapMemoryCacheParamsSupplier(bitmapMemoryCacheParamsSupplier)
                .setMainDiskCacheConfig(diskCacheConfig)
                .setDownsampleEnabled(true);
    }
}
