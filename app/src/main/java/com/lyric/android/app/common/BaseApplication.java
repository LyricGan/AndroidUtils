package com.lyric.android.app.common;

import android.app.Application;
import android.content.Context;

/**
 * 应用入口
 * @author lyricgan
 * @date 2017/11/29 10:10
 */
public abstract class BaseApplication extends Application {
    public final String TAG = getClass().getSimpleName();
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static BaseApplication getApplication() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    public abstract boolean isDebuggable();
}
