package com.lyric.android.library.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Set;

/**
 * @author ganyu
 * @description
 * @time 16/1/16 下午11:40
 */
public class PreferenceImpl<T> implements IPreference<T> {
    private static final String DEFAULT_SHARED_PREFERENCE_NAME = "shared_prefs";
    private SharedPreferences mPreferences;

    public PreferenceImpl(Context context) {
        this(context, DEFAULT_SHARED_PREFERENCE_NAME);
    }

    public PreferenceImpl(Context context, String sharedFileName) {
        if (context == null || TextUtils.isEmpty(sharedFileName)) {
            return;
        }
        mPreferences = context.getSharedPreferences(sharedFileName, Context.MODE_PRIVATE);
    }

    @Override
    public T read(String key) {
        return null;
    }

    @Override
    public void write(String key, T object) {
        SharedPreferences.Editor editor = mPreferences.edit();
        if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Set) {
            editor.putStringSet(key, (Set<String>) object);
        } else {
            return;
        }
        editor.commit();
    }

    @Override
    public boolean remove(String key) {
        SharedPreferences.Editor editor = mPreferences.edit();
        return editor.remove(key).commit();
    }

    @Override
    public boolean clear() {
        SharedPreferences.Editor editor = mPreferences.edit();
        return editor.clear().commit();
    }
}
