package com.lyric.android.app.utils.cache;

import java.util.HashMap;

/**
 * @author lyricgan
 * @description 磁盘缓存管理类
 * @time 16/1/17 下午10:43
 */
public class DiskCacheManager<T> {
    private static volatile DiskCacheManager mInstance;

    private DiskCacheManager() {
        if (mInstance != null) {
            throw new RuntimeException("mInstance is not null.");
        }
    }

    public static DiskCacheManager build() {
        if (mInstance == null) {
            synchronized (DiskCacheManager.class) {
                if (mInstance == null) {
                    mInstance = new DiskCacheManager();
                }
            }
        }
        return mInstance;
    }

    public boolean put(T object) {
        return true;
    }

    public boolean remove() {
        return true;
    }

    public T get(HashMap<String, Object> params) {
        return null;
    }
}
