package com.lyric.android.app.test.cache;

import android.support.v4.util.LruCache;

/**
 * 内存缓存管理类，使用LruCache实现
 * <p>
 *     1.从Java1.5开始支持;
 *     2.无偿提供序列化机制;
 *     3.绝对防止多次实例化，即使在面对复杂的序列化或者反射攻击的时候;
 * </p>
 * @author lyricgan
 * @time 16/1/17 下午10:43
 */
public enum MemoryCacheManager {
    instance;
    private LruCache<String, Object> mDataCache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 8));

    MemoryCacheManager() {
    }

    public LruCache<String, Object> getCache() {
        return mDataCache;
    }
}
