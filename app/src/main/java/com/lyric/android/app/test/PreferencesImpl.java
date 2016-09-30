package com.lyric.android.app.test;

import android.content.Context;
import android.text.TextUtils;

import com.lyric.android.app.base.BaseApp;

/**
 * @author lyricgan
 * @description impl of preferences
 * @time 2016/9/30 15:41
 */
public class PreferencesImpl extends Preferences {
    private static final String DEFAULT_PREFERENCE_NAME = "android_utils";
    private String mPreferencesName;

    public PreferencesImpl() {
        this(DEFAULT_PREFERENCE_NAME);
    }

    public PreferencesImpl(String name) {
        super();
        this.mPreferencesName = name;
    }

    @Override
    public String getName() {
        if (TextUtils.isEmpty(mPreferencesName)) {
            mPreferencesName = DEFAULT_PREFERENCE_NAME;
        }
        return mPreferencesName;
    }

    @Override
    public Context getContext() {
        return BaseApp.getContext();
    }

    public void setName(String name) {
        this.mPreferencesName = name;
    }
}
