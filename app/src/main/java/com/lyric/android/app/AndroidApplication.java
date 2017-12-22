package com.lyric.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.lyric.common.BaseApplication;
import com.lyric.android.app.utils.LogUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * 应用入口，进行初始化
 * @author lyricgan
 * @time 2015/10/7 14:04
 */
public class AndroidApplication extends BaseApplication {

	@Override
	public void onCreate() {
		super.onCreate();

        LogUtils.setDebug(isDebuggable());

        addRegisterActivityLifecycleCallbacks();
	}

    @Override
    public boolean isDebuggable() {
        return Constants.DEBUG;
    }

    protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    private void addRegisterActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                loggingMessage(activity, "onActivityCreated");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                loggingMessage(activity, "onActivityStarted");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                loggingMessage(activity, "onActivityResumed");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                loggingMessage(activity, "onActivityPaused");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                loggingMessage(activity, "onActivityStopped");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                loggingMessage(activity, "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                loggingMessage(activity, "onActivityDestroyed");
            }
        });
    }

    private void loggingMessage(Activity activity, String lifecycle) {
	    if (isDebuggable()) {
            Log.d(TAG, "activityName:" + activity.getClass().getName() + ",lifecycle:" + lifecycle);
        }
    }
}
