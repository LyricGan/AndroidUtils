package com.lyric.android.app.third.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.haiqiu.jihai.utils.StringUtil;

import java.io.IOException;

/**
 * 自定义整型数据解析
 * @author ganyu
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
        return StringUtil.parseInt(in.nextString(), 0);
    }
}
