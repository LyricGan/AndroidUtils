package com.android.mvvm;

import android.app.Application;
import android.content.Context;

/**
 * @author lyric
 * @description
 * @time 2016/5/27 13:11
 */
public class BaseApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static BaseApplication getApplication(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
