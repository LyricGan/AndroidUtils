package com.lyric.android.app.third.retrofit;

/**
 * @author lyricgan
 * @description 响应数据处理类
 * @time 2016/7/27 12:40
 */
public class ResponseHandler {

    private ResponseHandler() {
    }

    /**
     * 统一通过错误码来处理错误提示
     * @param errorCode 错误码
     * @param errorMessage 错误提示信息
     */
    public static void process(String errorCode, String errorMessage) {
    }

    public static void process(ResponseError error) {
    }
}
