package com.lyric.android.app;

import android.app.Application;
import android.content.Context;

import com.lyric.android.app.constants.Constants;
import com.lyric.android.library.utils.LogUtils;

/**
 * extends Application {@link Application}
 * 
 * @author ganyu
 * 
 */
public class BaseApplication extends Application {
	private static Context sContext;
//    private static RefWatcher mRefWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = this;
		
		LogUtils.setDebug(Constants.DEBUG);

//        if (Constants.LEAK_DEBUG) {
//            mRefWatcher = LeakCanary.install(this);
//        } else {
//            mRefWatcher = RefWatcher.DISABLED;
//        }
	}

	public static Context getContext() {
		return sContext;
	}

//    public static RefWatcher getRefWatcher() {
//        return mRefWatcher;
//    }
}
