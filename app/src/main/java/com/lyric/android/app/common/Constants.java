package com.lyric.android.app.common;

import com.lyric.android.app.BuildConfig;

/**
 * 应用常量接口
 * 
 * @author lyricgan
 * @created 2015-4-20
 */
public interface Constants {
    boolean DEBUG = BuildConfig.LOG_DEBUG;
    String TAG = "AndroidUtils";
    String BASE_URL = "https://api.github.com";

    String EXTRAS_DATA = "_data";
    String EXTRAS_URL = "_url";
}
