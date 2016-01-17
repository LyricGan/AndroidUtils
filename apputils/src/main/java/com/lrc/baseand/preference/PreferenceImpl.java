package com.lrc.baseand.preference;

/**
 * @author ganyu
 * @description
 * @time 16/1/16 下午11:40
 */
public class PreferenceImpl<T> implements IPreference<T> {

    @Override
    public T read() {
        return null;
    }

    @Override
    public void write(T Object) {

    }

    @Override
    public boolean remove(String key) {
        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }
}
