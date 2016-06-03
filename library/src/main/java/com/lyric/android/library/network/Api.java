package com.lyric.android.library.network;

/**
 * @author lyric
 * @description 网络接口
 * @time 2016/4/7 13:59
 */
public class Api implements BaseApi {
    private static Api mInstance;

    private Api() {
    }

    public static synchronized Api build() {
        if (mInstance == null) {
            mInstance = new Api();
        }
        return mInstance;
    }
}
