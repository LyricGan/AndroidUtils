package com.lyric.android.app;

import com.lyric.android.app.common.Constants;
import com.lyric.android.app.utils.LogUtils;
import com.lyric.android.app.common.BaseApplication;

/**
 * application for initialized
 * @author lyricgan
 */
public class AndroidApplication extends BaseApplication {

	@Override
	public void onCreate() {
		super.onCreate();

        LogUtils.setDebug(isDebuggable());
	}

    @Override
    public boolean isDebuggable() {
        return Constants.DEBUG;
    }
}
