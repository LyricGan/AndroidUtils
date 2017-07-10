package com.lyric.android.app.third.gson;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * 自定义整型数据解析，避免float转int出现的问题
 * @author lyricgan
 * @time 2017/7/10 12:56
 */
public class IntegerTypeAdapter extends TypeAdapter<Integer> {

    @Override
    public void write(JsonWriter out, Integer value) throws IOException {
        out.value(value);
    }

    @Override
    public Integer read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return 0;
        }
        try {
            return (int) in.nextDouble();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
}
