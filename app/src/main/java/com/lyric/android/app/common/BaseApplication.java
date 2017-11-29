package com.lyric.android.app.common;

import android.app.Application;

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

    public static Application getContext() {
        return mInstance;
    }
}
