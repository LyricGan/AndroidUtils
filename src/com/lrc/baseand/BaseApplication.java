package com.lrc.baseand;

import com.lrc.baseand.utils.LogUtils;

import android.app.Application;

/**
 * 应用程序入口，继承自 {@link Application}
 * 
 * @author ganyu
 * 
 */
public class BaseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		LogUtils.setDebug(true);
	}
	
}
