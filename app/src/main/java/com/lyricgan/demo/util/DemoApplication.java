package com.lyricgan.demo.util;

import android.app.Application;
import android.content.Context;

/**
 * application for initialized
 * @author Lyric Gan
 */
public class DemoApplication extends Application {
    private static DemoApplication sInstance;

	@Override
	public void onCreate() {
		super.onCreate();
        sInstance = this;
	}

    public static Context getContext() {
        return sInstance.getApplicationContext();
    }
}
