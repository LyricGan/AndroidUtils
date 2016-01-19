package com.lyric.android.app.net.core;

import android.content.Context;

import com.lyric.android.app.net.RequestQueue;

import java.io.File;

public class Volley {
    /** 默认磁盘缓存目录 */
    private static final String DEFAULT_CACHE_DIR = "http_cache";

    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     * You may set a maximum size of the disk cache in bytes.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack An {@link HurlStack} to use for the network, or null for default.
     * @param maxDiskCacheBytes the maximum size of the disk cache, in bytes. Use -1 for default size.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context, HurlStack stack, int maxDiskCacheBytes) {
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
        if (stack == null) {
        	stack = new HurlStack();
        }
        Network network = new Network(stack);
        RequestQueue queue;
        // 判断是否设置磁盘缓存最大值
		if (maxDiskCacheBytes <= -1) {
			queue = new RequestQueue(new DiskBasedCache(cacheDir), network);
		} else {
			queue = new RequestQueue(new DiskBasedCache(cacheDir, maxDiskCacheBytes), network);
		}
        queue.start();
        return queue;
    }
    
    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     * You may set a maximum size of the disk cache in bytes.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param maxDiskCacheBytes the maximum size of the disk cache, in bytes. Use -1 for default size.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context, int maxDiskCacheBytes) {
        return newRequestQueue(context, null, maxDiskCacheBytes);
    }
    
    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack An {@link HurlStack} to use for the network, or null for default.
     * @return A started {@link RequestQueue} instance.
     */
	public static RequestQueue newRequestQueue(Context context, HurlStack stack) {
		return newRequestQueue(context, stack, -1);
	}
    
    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, null);
    }

}

