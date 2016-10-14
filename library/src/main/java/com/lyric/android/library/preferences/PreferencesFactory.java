package com.lyric.android.library.preferences;

import android.content.Context;

/**
 * for example
 * <pre>
 * class Preferences extends PreferencesImpl {
 *  public static Preferences mInstance;
 *
 *  private Preferences() {}
 *
 *  public static Preferences getInstance() {
 *      if (mInstance == null) {
 *          mInstance = new Preferences();
 *      }
 *      return mInstance;
 *  }
 *
 *  @Override
 *  public Context getContext() {
 *      return BaseApp.getContext();
 *  }
 * }<pre>
 * @see PreferencesImpl
 *
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
