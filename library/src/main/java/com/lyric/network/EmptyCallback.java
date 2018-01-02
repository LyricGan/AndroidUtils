package com.lyric.network;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * 请求数据响应回调接口，空实现
 * @author lyricgan
 * @date 2018/1/2 14:55
 */
public class EmptyCallback<T> extends BaseResponseCallback<T> {

    @Override
    public void onFailure(NetworkRequest networkRequest, IOException e) {
    }

    @Override
    public T parseResponse(ResponseBody responseBody) {
        return null;
    }

    @Override
    public void onResponse(NetworkRequest networkRequest, T result) {
    }
}
