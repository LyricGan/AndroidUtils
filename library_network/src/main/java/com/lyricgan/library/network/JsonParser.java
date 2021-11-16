package com.lyricgan.library.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * json解析工具
 * @author Lyric Gan
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
