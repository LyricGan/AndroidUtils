package com.lyric.android.app.retrofit;

/**
 * 网络请求错误定义
 * @author lyricgan
 * @time 2016/9/5 10:38
 */
public class ResponseError {
    public static final int ERROR_REQUEST_FAILED = -1;
    public static final int ERROR_DATA_EXCEPTION = -2;

    private int code;
    private String message;

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
