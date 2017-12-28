package com.lyric.network;

import okhttp3.Response;

/**
 * 网络响应包装类
 *
 * @author lyricgan
 * @date 2017/12/28 10:40
 */
public class NetworkResponse {
    private Response response;

    public NetworkResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
