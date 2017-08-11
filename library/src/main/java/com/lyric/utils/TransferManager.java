package com.lyric.utils;

import android.util.LruCache;

/**
 * 传递工具类，解决对象传递需要序列化的问题，通过将对象存放在内存中来实现类与类之间对象的传递
 * @author lyricgan
 * @time 2016/10/20 11:47
 */
public enum TransferManager {
    instance;

    private LruCache<String, Object> mCache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 8));

    public Object put(String key, Object value) {
        return mCache.put(key, value);
    }

    public Object get(String key) {
        return mCache.get(key);
    }

    public Object remove(String key) {
        return mCache.remove(key);
    }
}
