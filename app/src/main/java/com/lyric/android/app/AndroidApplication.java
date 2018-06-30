package com.lyric.android.app;

import android.app.Application;
import android.content.Context;

import com.lyric.android.app.common.Constants;
import com.lyric.android.app.utils.LogUtils;

/**
 * application for initialized
 * @author lyricgan
 */
public class AndroidApplication extends Application {
    private static AndroidApplication sInstance;

	@Override
	public void onCreate() {
		super.onCreate();
        sInstance = this;

        LogUtils.setDebug(Constants.DEBUG);
	}

    public static Context getContext() {
        return sInstance.getApplicationContext();
    }
}
