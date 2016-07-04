package com.lyric.android.app.cache;

import java.util.HashMap;

/**
 * @author lyricgan
 * @description 缓存接口类
 * @time 16/1/17 下午10:44
 */
public interface CacheFactory<T> {

    boolean cacheInMemory(T object);

    boolean cacheInDisk(T object);

    T getFromMemory(HashMap<String, Object> params);

    T getFromDisk(HashMap<String, Object> params);

    T getFromNetwork(HashMap<String, Object> params);
}
