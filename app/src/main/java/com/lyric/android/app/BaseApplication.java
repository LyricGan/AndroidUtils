package com.lyric.android.app;

import android.app.Application;
import android.content.Context;

import com.lyric.android.app.constants.AppConstants;
import com.lyric.android.library.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * extends Application {@link Application}
 * 
 * @author ganyu
 * 
 */
public class BaseApplication extends Application {
	private static Context mContext;
    private static RefWatcher mRefWatcher;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		
		LogUtils.setDebug(AppConstants.DEBUG);

        if (AppConstants.LEAK_DEBUG) {
            mRefWatcher = LeakCanary.install(this);
        } else {
            mRefWatcher = RefWatcher.DISABLED;
        }
	}

	public static Context getContext() {
		return mContext;
	}

    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

}
