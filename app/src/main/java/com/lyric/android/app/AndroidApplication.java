package com.lyric.android.app;

import com.lyric.android.app.common.Constants;
import com.lyric.android.app.utils.LogUtils;
import com.lyric.android.app.common.BaseApplication;

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
	}

    @Override
    public boolean isDebuggable() {
        return Constants.DEBUG;
    }
}
