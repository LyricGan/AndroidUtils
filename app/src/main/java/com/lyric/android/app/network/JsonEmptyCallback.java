package com.lyric.android.app.network;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Json数据请求回调接口
 * @author lyricgan
 * @date 2017/12/28 14:32
 */
public class JsonEmptyCallback<T> extends JsonCallback<T> {

    public JsonEmptyCallback(Type type) {
        super(type);
    }

    @Override
    public void onFailure(HttpRequest httpRequest, IOException e) {
    }

    @Override
    public void onResponse(HttpRequest httpRequest, T result) {
    }
}
