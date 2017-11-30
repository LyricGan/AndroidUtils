package com.lyric.android.app.test.cache;

/**
 * 缓存类型，分为内存缓存、磁盘缓存和网络缓存
 * @author lyricgan
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
