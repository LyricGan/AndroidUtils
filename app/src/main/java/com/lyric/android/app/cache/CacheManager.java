package com.lyric.android.app.cache;

import java.util.HashMap;

/**
 * @author lyricgan
 * @description 缓存管理类，默认不做磁盘缓存
 * @time 16/1/17 下午9:36
 */
public abstract class CacheManager<T> implements CacheFactory<T> {
    private String mKey;

    public CacheManager(String key) {
        setKey(key);
    }

    public void setKey(String key) {
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
        if (object == null) {
            return false;
        }
        MemoryCacheManager.instance.put(mKey, object);
        return true;
    }

    @Override
    public boolean cacheInDisk(T object) {
        return false;
    }

    @Override
    public T getFromMemory(HashMap<String, Object> params) {
        if (mKey == null) {
            return null;
        }
        return (T) MemoryCacheManager.instance.get(mKey);
    }

    @Override
    public T getFromDisk(HashMap<String, Object> params) {
        return null;
    }
}
