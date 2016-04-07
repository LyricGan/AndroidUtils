package com.lyric.android.library.cache;

/**
 * @author ganyu
 * @description cache type
 * @time 16/1/17 下午10:41
 */
public enum CacheType {
    MEMORY(1),
    DISK(2),
    NETWORK(3);

    int value;

    CacheType(int value) {
        this.value = value;
    }

    int getValue() {
        return value;
    }
}
