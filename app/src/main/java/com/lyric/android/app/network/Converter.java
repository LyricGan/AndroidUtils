package com.lyric.android.app.network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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
        T result;
        // 字符串直接返回，不做转换处理
        if (String.class.getClass().equals(type.getClass())) {
            result = (T) json;
        } else {
            try {
                result = mGson.fromJson(json, type);
            } catch (JsonSyntaxException e) {
                result = null;
            }
        }
        return result;
    }
}
