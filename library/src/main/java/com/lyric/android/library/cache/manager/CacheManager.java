package com.lyric.android.library.cache.manager;

import com.lyric.android.library.cache.core.CacheFactory;

/**
 * @author ganyu
 * @description cache manager
 * @time 16/1/17 下午9:36
 */
public abstract class CacheManager<T> implements CacheFactory<T> {

    @Override
    public void cacheInMemory(T object) {

    }

    @Override
    public void cacheInDisk(T object) {

    }

    @Override
    public T getFromMemory(String key) {
        return null;
    }

    @Override
    public T getFromDisk(String key) {
        return null;
    }

    @Override
    public T getFromNetwork(String key) {
        return null;
    }
}
