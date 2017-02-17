package com.lyric.android.app.utils.cache;

import android.support.v4.util.LruCache;

/**
 * @author lyricgan
 * @description 内存缓存
 * @time 16/1/17 下午10:43
 */
public enum MemoryCacheManager {
    /**
     * 1.从Java1.5开始支持;
     * 2.无偿提供序列化机制;
     * 3.绝对防止多次实例化，即使在面对复杂的序列化或者反射攻击的时候;
     */
    instance;

    private LruCache<String, Object> mDataCache = new LruCache<String, Object>((int) (Runtime.getRuntime().maxMemory() / 8));

    MemoryCacheManager() {
    }

    public void remove(String key) {
        mDataCache.remove(key);
    }

    public Object get(String key) {
        return mDataCache.get(key);
    }

    public Object put(String key, Object value) {
        return mDataCache.put(key, value);
    }
}
