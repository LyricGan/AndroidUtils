package com.lyric.android.library.cache;

import com.lyric.android.library.network.Api;

/**
 * @author ganyu
 * @description 网络管理类
 * @time 16/1/17 下午10:43
 */
public class NetworkManager {
    private static Api mInstance;

    private NetworkManager() {
    }

    public static synchronized Api getApi() {
        if (mInstance == null) {
            mInstance = Api.build();
        }
        return mInstance;
    }
}
