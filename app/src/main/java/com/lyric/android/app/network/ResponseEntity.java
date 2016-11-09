package com.lyric.android.app.network;

/**
 * @author lyric
 * @description 网络响应数据实体类
 * @time 2016/6/22 14:45
 */
public class ResponseEntity {
    private String url;
    private String params;
    private String headers;
    private int responseCode;
    private String response;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return (200 == responseCode);
    }
}
