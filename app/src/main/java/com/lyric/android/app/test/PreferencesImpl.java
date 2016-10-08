package com.lyric.android.app.test;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.lyric.android.library.preferences.PreferencesBaseImpl;

/**
 * @author lyricgan
 * @description implement of preferences
 * @time 2016/9/30 15:41
 */
public abstract class PreferencesImpl extends PreferencesBaseImpl {
    private static final String DEFAULT_PREFERENCE_NAME = "android_utils";
    private String mPreferencesName;
    private Gson mGson;

    public PreferencesImpl() {
        this(DEFAULT_PREFERENCE_NAME);
    }

    public PreferencesImpl(String name) {
        super();
        this.mPreferencesName = name;
        this.mGson = new Gson();
    }

    public String getName() {
        return mPreferencesName;
    }

    public void setName(String name) {
        this.mPreferencesName = name;
    }

    @Override
    public SharedPreferences getSharedPreferences()  {
        return getContext().getSharedPreferences(getName(), Context.MODE_PRIVATE);
    }

    public <T> T get(String key, Class<T> clazz) {
        return mGson.fromJson(getString(key), clazz);
    }

    public <T> void put(String key, T object) {
        putString(key,  mGson.toJson(object));
    }

    public abstract Context getContext();
}
