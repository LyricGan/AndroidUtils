package com.lrc.baseand.preference;

/**
 * @author ganyu
 * @description
 * @time 16/1/16 下午11:38
 */
public interface IPreference<T> {

    T read();

    void write(T Object);

    boolean remove(String key);

    boolean clear();
}
