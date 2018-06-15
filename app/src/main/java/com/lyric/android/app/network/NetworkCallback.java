package com.lyric.android.app.network;

import java.io.IOException;

/**
 * 网络请求回调接口
 * @author lyricgan
 * @date 2017/12/28 14:25
 */
public interface NetworkCallback {

    void onFailure(NetworkRequest networkRequest, IOException e);

    void onResponse(NetworkRequest networkRequest, NetworkResponse networkResponse);

    void onCancel(NetworkRequest networkRequest);
}
