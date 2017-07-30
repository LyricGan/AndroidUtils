package com.lyric.android.app.utils;

import com.lyric.android.app.BaseApp;
import com.lyric.android.library.preferences.PreferencesHelper;

/**
 * @author lyricgan
 * @description
 * @time 2016/10/8 15:14
 */
public class Preferences extends PreferencesHelper {
    public static final String DEFAULT_NAME = "android_utils";
    private static Preferences mInstance;

    private Preferences() {
        super(BaseApp.getContext(), DEFAULT_NAME);
    }

    public static Preferences getInstance() {
        if (mInstance == null) {
            mInstance = new Preferences();
        }
        return mInstance;
    }
}
