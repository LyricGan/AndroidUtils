package com.lyric.android.app.network;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * @author lyricgan
 * @description converter for json
 * @time 2016/7/7 14:51
 */
public class Converter {
    private final Gson mGson;
    private static Converter mInstance;

    private Converter() {
        mGson = new Gson();
    }

    public static synchronized Converter getInstance() {
        if (mInstance == null) {
            mInstance = new Converter();
        }
        return mInstance;
    }

    public <T> T convert(String json, Type type) {
        // 字符串直接返回，不做转换处理
        if (String.class.getClass().equals(type.getClass())) {
            return (T) json;
        } else {
            return mGson.fromJson(json, type);
        }
    }
}
