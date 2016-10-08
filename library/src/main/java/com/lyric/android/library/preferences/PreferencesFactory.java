package com.lyric.android.library.preferences;

import android.content.Context;

/**
 * @author lyricgan
 * @description factory of {@link android.content.SharedPreferences}
 * @time 2016/9/30 15:47
 */
public class PreferencesFactory {
    private static final String DEFAULT_PREFERENCE_NAME = "android_utils";
    private static PreferencesImpl mPreferencesImpl;

    public static PreferencesImpl getDefault(Context context) {
        return getDefault(context, DEFAULT_PREFERENCE_NAME);
    }

    public static PreferencesImpl getDefault(Context context, String name) {
        if (mPreferencesImpl == null) {
            mPreferencesImpl = new PreferencesImpl(context, name);
        }
        return mPreferencesImpl;
    }
}
