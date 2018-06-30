package com.lyric.android.app.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Json parse with gson
 *
 * @author lyricgan
 */
public class JsonParser {
    private Gson mGson;

    private JsonParser() {
        mGson = getDefaultGson();
    }

    private static class JsonHelperHolder {
        private static final JsonParser JSON_HELPER = new JsonParser();
    }

    public static JsonParser getInstance() {
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
        this.mGson = gson;
    }

    public <T> T parse(String json, Class<T> classOfT) {
        return getGson().fromJson(json, classOfT);
    }

    public <T> T parse(String json, Type typeOfT) {
        return getGson().fromJson(json, typeOfT);
    }
}
