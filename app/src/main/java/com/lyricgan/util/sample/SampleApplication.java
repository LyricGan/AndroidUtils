package com.lyricgan.util.sample;

import android.app.Application;

import com.lyricgan.util.ApplicationUtils;

/**
 * 示例应用
 * @author Lyric Gan
 */
public class SampleApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ApplicationUtils.setApplication(this);
	}
}
