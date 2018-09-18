package com.lyric.android.app.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Json parse with gson
 *
 * @author lyricgan
 */
public class JsonParser {
    private Gson mParser;

    private JsonParser() {
        mParser = getDefaultParser();
    }

    private static class JsonParserHolder {
        private static final JsonParser JSON_HELPER = new JsonParser();
    }

    public static JsonParser getInstance() {
        return JsonParserHolder.JSON_HELPER;
    }

    private Gson getDefaultParser() {
        return new GsonBuilder().create();
    }

    public Gson getParser() {
        return mParser;
    }

    public void setParser(Gson parser) {
        this.mParser = parser;
    }
}
