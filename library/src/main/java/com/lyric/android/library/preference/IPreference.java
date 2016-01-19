package com.lyric.android.library.preference;

/**
 * @author ganyu
 * @description
 * @time 16/1/16 下午11:38
 */
public interface IPreference<T> {

    T read(String key) throws InterruptedException;

    void write(String key, T object) throws InterruptedException;

    boolean remove(String key) throws InterruptedException;

    boolean clear() throws InterruptedException;
}
