package com.lyric.android.library.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * @author lyricgan
 * @description implement of preferences
 * @time 2016/9/30 15:41
 */
public class PreferencesImpl extends PreferencesBaseImpl {
    private String mPreferencesName;
    private Gson mGson;
    private Context mContext;

    public PreferencesImpl(Context context, String name) {
        super();
        this.mContext = context.getApplicationContext();
        this.mPreferencesName = name;
        this.mGson = new Gson();
    }

    public String getName() {
        return mPreferencesName;
    }

    public Context getContext() {
        return mContext;
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
}
