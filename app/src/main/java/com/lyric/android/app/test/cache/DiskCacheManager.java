package com.lyric.android.app.test.cache;

import java.util.HashMap;

/**
 * 磁盘缓存管理类
 * @author lyricgan
 * @time 16/1/17 下午10:43
 */
public class DiskCacheManager {
    private static volatile DiskCacheManager mInstance;

    private DiskCacheManager() {
        if (mInstance != null) {
            throw new RuntimeException("mInstance is not null.");
        }
    }

    public static DiskCacheManager getInstance() {
        if (mInstance == null) {
            synchronized (DiskCacheManager.class) {
                if (mInstance == null) {
                    mInstance = new DiskCacheManager();
                }
            }
        }
        return mInstance;
    }

    public Object put(String key, Object object) {
        return null;
    }

    public boolean remove() {
        return true;
    }

    public Object get(HashMap<String, Object> params) {
        return null;
    }
}
