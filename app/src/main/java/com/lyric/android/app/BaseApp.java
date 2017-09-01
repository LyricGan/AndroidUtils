package com.lyric.android.app;

import android.app.Application;
import android.content.Context;

import com.lyric.android.app.third.fresco.ImageHelper;
import com.lyric.android.app.third.stetho.StethoUtils;
import com.lyric.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * 应用入口，进行初始化
 * @author lyricgan
 * @time 2015/10/7 14:04
 */
public class BaseApp extends Application implements Constants {
    private static BaseApp mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
        mInstance = this;

        LogUtils.setDebug(DEBUG);
        StethoUtils.initialize(this, DEBUG);
        ImageHelper.getInstance().initialize(this);

        if (LEAK_DEBUG) {
            setupLeakCanary();
        }
	}

	public static Context getContext() {
		return mInstance.getApplicationContext();
	}

    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }
}
