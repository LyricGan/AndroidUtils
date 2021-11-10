package com.lyricgan.demo.util.network;

import java.io.IOException;

/**
 * 网络请求回调接口
 * @author Lyric Gan
 * @since 2017/12/28 14:25
 */
public interface HttpCallback {

    void onFailure(HttpRequest httpRequest, IOException e);

    void onResponse(HttpRequest httpRequest, HttpResponse httpResponse);

    void onCancel(HttpRequest httpRequest);
}
