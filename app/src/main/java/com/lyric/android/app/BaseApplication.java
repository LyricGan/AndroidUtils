package com.lyric.android.app;

import android.app.Application;
import android.content.Context;

import com.lyric.android.library.log.MultiLog;

/**
 * extends Application {@link Application}
 * 
 * @author ganyu
 * 
 */
public class BaseApplication extends Application {
	public static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = this;
		
		MultiLog.setDebug(true);
	}

	public static Context getContext() {
		return sContext;
	}
}
