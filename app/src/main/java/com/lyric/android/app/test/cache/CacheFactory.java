package com.lyric.android.app.test.cache;

import java.util.HashMap;

/**
 * 缓存接口类
 * @author lyricgan
 * @time 16/1/17 下午10:44
 */
public interface CacheFactory<T> {

    boolean cacheInMemory(T object);

    boolean cacheInDisk(T object);

    T getFromMemory(HashMap<String, Object> params);

    T getFromDisk(HashMap<String, Object> params);

    T getFromNetwork(HashMap<String, Object> params);
}
