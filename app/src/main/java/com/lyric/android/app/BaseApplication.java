package com.lyric.android.app;

import android.app.Application;
import android.content.Context;

import com.lyric.android.library.log.Loggers;

/**
 * extends Application {@link Application}
 * 
 * @author ganyu
 * 
 */
public class BaseApplication extends Application {
	private static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = this;
		
		Loggers.setDebug(true);
	}

	public static Context getContext() {
		return sContext;
	}
}
