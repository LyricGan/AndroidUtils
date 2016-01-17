package com.lrc.baseand;

import android.app.Application;
import android.content.Context;

import com.lrc.baseand.utils.LogUtils;

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
		
		LogUtils.setDebug(true);
	}

	public static Context getContext() {
		return sContext;
	}
}
