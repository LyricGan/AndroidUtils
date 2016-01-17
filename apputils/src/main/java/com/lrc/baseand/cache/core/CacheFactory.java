package com.lrc.baseand.cache.core;

/**
 * @author ganyu
 * @description cache factory for manager
 * @time 16/1/17 下午10:44
 */
public interface CacheFactory<T> {

    void cacheInMemory(T object);

    void cacheInDisk(T object);

    T getFromMemory(String key);

    T getFromDisk(String key);

    T getFromNetwork(String key);
}
