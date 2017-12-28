package com.lyric.network;

import com.lyric.data.JsonHelper;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * Json数据请求回调接口
 * @author lyricgan
 * @date 2017/12/28 11:44
 */
public abstract class JsonCallback<T> extends BaseCallback<T> {
    private Type type;

    public JsonCallback(Type type) {
        this.type = type;
    }

    @Override
    public T parseResponse(ResponseBody responseBody) {
        try {
            return JsonHelper.getInstance().parse(responseBody.string(), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
