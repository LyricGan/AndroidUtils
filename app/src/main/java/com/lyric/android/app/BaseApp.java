package com.lyric.android.app;

import android.app.Application;
import android.content.Context;

import com.lyric.android.app.third.fresco.ImageHelper;
import com.lyric.android.app.third.stetho.StethoUtils;
import com.lyric.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * @author lyricgan
 * @description
 * @time 2015/10/7 14:04
 */
public class BaseApp extends Application {
    private static BaseApp mInstance;
    private static RefWatcher mRefWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
        mInstance = this;

        LogUtils.setDebug(Constants.DEBUG);
        initRefWatcher(Constants.LEAK_DEBUG);
        StethoUtils.initialize(this, Constants.DEBUG);
        ImageHelper.getInstance().initialize(this);
	}

	public static Context getContext() {
		return mInstance.getApplicationContext();
	}

    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    private void initRefWatcher(boolean isDebug) {
        if (isDebug) {
            mRefWatcher = LeakCanary.install(this);
        } else {
            mRefWatcher = RefWatcher.DISABLED;
        }
    }
}
