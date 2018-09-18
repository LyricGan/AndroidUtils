package com.lyric.android.app.network;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * 请求数据响应回调接口，空实现
 * @author lyricgan
 * @date 2018/1/2 14:55
 */
public class EmptyCallback<T> extends BaseResponseCallback<T> {

    @Override
    public void onFailure(HttpRequest httpRequest, IOException e) {
    }

    @Override
    public T parseResponse(ResponseBody responseBody) {
        return null;
    }

    @Override
    public void onResponse(HttpRequest httpRequest, T result) {
    }
}
