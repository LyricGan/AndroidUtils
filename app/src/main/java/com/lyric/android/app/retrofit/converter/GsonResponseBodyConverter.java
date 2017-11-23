package com.lyric.android.app.retrofit.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/28 11:14
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson mGson;
    private final TypeAdapter<T> mTypeAdapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.mGson = gson;
        this.mTypeAdapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = mGson.newJsonReader(value.charStream());
        try {
            return mTypeAdapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}