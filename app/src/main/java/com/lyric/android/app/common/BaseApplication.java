package com.lyric.android.app.common;

import android.app.Application;
import android.content.Context;

/**
 * 应用入口
 * @author lyricgan
 * @date 2017/11/29 10:10
 */
public class BaseApplication extends Application {
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Application getApplication() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }
}
