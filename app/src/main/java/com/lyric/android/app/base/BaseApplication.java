package com.lyric.android.app.base;

import android.app.Application;

import com.lyric.android.app.constants.Constants;
import com.lyric.android.library.utils.LogUtils;

/**
 * extends Application {@link Application}
 * 
 * @author ganyu
 * 
 */
public class BaseApplication extends Application {
    private static BaseApplication sApplication;
//    private static RefWatcher mRefWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
        sApplication = this;

		LogUtils.setDebug(Constants.DEBUG);

//        if (Constants.LEAK_DEBUG) {
//            mRefWatcher = LeakCanary.install(this);
//        } else {
//            mRefWatcher = RefWatcher.DISABLED;
//        }
	}

	public static BaseApplication getContext() {
		return sApplication;
	}

//    public static RefWatcher getRefWatcher() {
//        return mRefWatcher;
//    }
}
