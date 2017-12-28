package com.lyric.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreferences工具类
 * @author lyricgan
 * @date 2017/12/28 15:26
 */
public class PreferencesHelper {

    private PreferencesHelper() {
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
