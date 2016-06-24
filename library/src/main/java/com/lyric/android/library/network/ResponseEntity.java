package com.lyric.android.library.network;

/**
 * @author lyric
 * @description 网络响应数据实体类
 * @time 2016/6/22 14:45
 */
public class ResponseEntity {
    public String url;
    public String params;
    public String headers;
    public int responseCode;
    public String response;

    public boolean isSuccess() {
        return (200 == responseCode);
    }
}
