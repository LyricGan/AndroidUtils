package com.lyric.android.library.network;

/**
 * @author <a href="mailto:ganyu@medlinker.net">ganyu</a>
 * @version 3.1
 * @description 网络接口
 * @time 2016/4/7 13:59
 */
public class Api {
    private static Api mInstance;

    Api() {
    }

    public static synchronized Api build() {
        if (mInstance == null) {
            mInstance = new Api();
        }
        return mInstance;
    }
}
