package com.lyric.android.library.preferences;

import android.content.Context;

/**
 * @author lyricgan
 * @description factory of {@link android.content.SharedPreferences}
 * @time 2016/9/30 15:47
 */
public class PreferencesFactory {
    private static final String DEFAULT_NAME = "android_utils";
    private static PreferencesHelper mHelper;

    public static PreferencesHelper getDefault(Context context) {
        return getDefault(context, DEFAULT_NAME);
    }

    public static PreferencesHelper getDefault(Context context, String name) {
        if (mHelper == null) {
            mHelper = new PreferencesHelper(context, name);
        }
        return mHelper;
    }
}
