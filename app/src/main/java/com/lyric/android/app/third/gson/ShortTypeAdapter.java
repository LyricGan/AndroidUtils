package com.lyric.android.app.third.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.lyric.utils.StringUtils;

import java.io.IOException;

/**
 * @author ganyu
 * @date 2017/7/25 10:21
 */
public class ShortTypeAdapter extends TypeAdapter<Short> {

    @Override
    public void write(JsonWriter out, Short value) throws IOException {
        out.value(value);
    }

    @Override
    public Short read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return 0;
        }
        return StringUtils.parseShort(in.nextString(), (short) 0);
    }
}
