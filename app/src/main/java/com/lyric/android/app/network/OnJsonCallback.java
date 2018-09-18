package com.lyric.android.app.network;

import com.lyric.android.app.common.JsonParser;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * Json数据请求回调接口
 * @author lyricgan
 * @date 2017/12/28 11:44
 */
public abstract class OnJsonCallback<T> extends HttpResponseCallback<T> {
    private Type type;

    public OnJsonCallback(Type type) {
        this.type = type;
    }

    @Override
    public T parseResponse(ResponseBody responseBody) {
        try {
            return JsonParser.getInstance().getParser().fromJson(responseBody.string(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
