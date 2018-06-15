package com.lyric.android.app.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Json数据解析工具<br/>
 * 使用Gson来做数据解析
 *
 * @author lyricgan
 */
public class JsonHelper {
    private Gson mGson;

    private JsonHelper() {
        mGson = getDefaultGson();
    }

    private static class JsonHelperHolder {
        private static final JsonHelper JSON_HELPER = new JsonHelper();
    }

    public static JsonHelper getInstance() {
        return JsonHelperHolder.JSON_HELPER;
    }

    public Gson getDefaultGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    public Gson getGson() {
        return mGson;
    }

    public void setGson(Gson gson) {
        if (gson == null) {
            gson = getDefaultGson();
        }
        this.mGson = gson;
    }

    public <T> T parse(String json, Class<T> classOfT) {
        return getGson().fromJson(json, classOfT);
    }

    public <T> T parse(String json, Type typeOfT) {
        return getGson().fromJson(json, typeOfT);
    }
}
