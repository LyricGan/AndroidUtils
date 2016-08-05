package com.lyric.android.app.base;

import android.app.Application;

import com.lyric.android.app.fresco.ImageHelper;
import com.lyric.android.app.stetho.StethoUtils;
import com.lyric.android.library.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * @author lyricgan
 * @description
 * @time 2015/10/7 14:04
 */
public class App extends Application {
    private static App mApp;
    private static RefWatcher mRefWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
        mApp = this;

        LogUtils.setDebug(Constants.DEBUG);
        initRefWatcher(Constants.LEAK_DEBUG);
        StethoUtils.initialize(this, Constants.DEBUG);
        ImageHelper.getInstance().initialize(this);
	}

	public static App getContext() {
		return mApp;
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
