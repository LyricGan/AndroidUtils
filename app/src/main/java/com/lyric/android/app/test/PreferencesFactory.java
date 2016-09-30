package com.lyric.android.app.test;

/**
 * @author lyricgan
 * @description factory of {@link android.content.SharedPreferences}
 * @time 2016/9/30 15:47
 */
public class PreferencesFactory {
    private static PreferencesImpl mPreferencesImpl;

    public static PreferencesImpl getPreferences() {
        return getPreferences("");
    }

    public static PreferencesImpl getPreferences(String name) {
        if (mPreferencesImpl == null) {
            mPreferencesImpl = new PreferencesImpl(name);
        }
        return mPreferencesImpl;
    }
}
