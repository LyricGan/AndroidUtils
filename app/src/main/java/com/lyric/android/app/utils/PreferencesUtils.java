package com.lyric.android.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreferences工具类
 * @author lyricgan
 * @time 2016/9/30 15:41
 */
public class PreferencesUtils {

    private PreferencesUtils() {
    }

    public static SharedPreferences.Editor getEditor(Context context, String name) {
        return getEditor(context, name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context, String name, int mode) {
        return getPreferences(context, name, mode).edit();
    }

    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences getPreferences(Context context, String name) {
        return getPreferences(context, name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getPreferences(Context context, String name, int mode) {
        return context.getSharedPreferences(name, mode);
    }
}
