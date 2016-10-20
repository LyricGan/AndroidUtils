package com.lyric.android.library.utils;

import android.util.LruCache;

/**
 * @author lyricgan
 * @description 解决对象传递需要序列化的问题，通过将对象存放在内存中来实现类与类之间对象的传递
 * @time 2016/10/20 11:47
 */
public enum ObjectTransferManager {
    instance;

    private LruCache<Object, Object> mCache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 8));

    public Object put(Object key, Object value) {
        return mCache.put(key, value);
    }

    public Object get(Object key) {
        return mCache.get(key);
    }

    public Object remove(Object key) {
        return mCache.remove(key);
    }
}
