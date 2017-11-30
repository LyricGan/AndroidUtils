package com.lyric.android.app.test.cache;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * 缓存管理类，依次读取内存缓存、磁盘缓存、网络缓存
 * @author lyricgan
 * @time 16/1/17 下午9:36
 */
public abstract class CacheManager<T> implements CacheFactory<T> {
    private String mKey;

    public CacheManager(String key) {
        this.mKey = key;
    }

    public T get(boolean refresh, HashMap<String, Object> param) {
        T object;
        if (refresh) {
            object = getFromNetwork(param);
            if (object != null) {
                cacheInDisk(object);
            }
        } else {
            object = getFromMemory(param);
            if (object != null) {
                return object;
            }
            object = getFromDisk(param);
            if (object != null) {
                cacheInMemory(object);
                return object;
            }
            object = getFromNetwork(param);
            if (object != null) {
                cacheInDisk(object);
            }
        }
        return object;
    }

    @Override
    public boolean cacheInMemory(T object) {
        if (TextUtils.isEmpty(mKey)) {
            return false;
        }
        if (object == null) {
            return false;
        }
        MemoryCacheManager.instance.getCache().put(mKey, object);
        return true;
    }

    @Override
    public boolean cacheInDisk(T object) {
        if (TextUtils.isEmpty(mKey)) {
            return false;
        }
        if (object == null) {
            return false;
        }
        DiskCacheManager.getInstance().put(mKey, object);
        return true;
    }

    @Override
    public T getFromMemory(HashMap<String, Object> params) {
        return (T) MemoryCacheManager.instance.getCache().get(mKey);
    }

    @Override
    public T getFromDisk(HashMap<String, Object> params) {
        return (T) DiskCacheManager.getInstance().get(params);
    }
}
