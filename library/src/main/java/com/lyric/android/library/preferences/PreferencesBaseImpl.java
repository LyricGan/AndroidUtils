package com.lyric.android.library.preferences;

import android.content.SharedPreferences;

/**
 * @author lyricgan
 * @description base implement of {@link SharedPreferences}
 * @time 2016/9/30 15:30
 */
public abstract class PreferencesBaseImpl {

    public void putString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    public void putLong(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).apply();
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    public long getLong(String key, long defaultValue) {
        return getSharedPreferences().getLong(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        getSharedPreferences().edit().putFloat(key, value).apply();
    }

    public float getFloat(String key) {
        return getFloat(key, 0f);
    }

    public float getFloat(String key, float defaultValue) {
        return getSharedPreferences().getFloat(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    public void remove(String key) {
        getSharedPreferences().edit().remove(key).apply();
    }

    public void clear() {
        getSharedPreferences().edit().clear().apply();
    }

    public abstract SharedPreferences getSharedPreferences();
}
