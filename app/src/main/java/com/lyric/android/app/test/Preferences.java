package com.lyric.android.app.test;

import android.content.Context;

import com.lyric.android.app.base.BaseApp;

/**
 * @author lyricgan
 * @description
 * @time 2016/10/8 15:14
 */
public class Preferences extends PreferencesImpl {
    public static Preferences mInstance;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (mInstance == null) {
            mInstance = new Preferences();
        }
        return mInstance;
    }

    @Override
    public Context getContext() {
        return BaseApp.getContext();
    }
}
