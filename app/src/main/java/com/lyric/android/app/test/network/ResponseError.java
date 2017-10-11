package com.lyric.android.app.test.network;

/**
 * @author lyric
 * @description
 * @time 2016/6/22 15:34
 */
public class ResponseError {
    private String url;
    private String params;
    private int code;
    private String message;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
