package com.lyric.android.app.network;

import okhttp3.Response;

/**
 * 网络响应包装类
 *
 * @author lyricgan
 * @date 2017/12/28 10:40
 */
public class HttpResponse {
    private Response response;

    public HttpResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
