package com.lyric.android.app.third.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * 自定义json对象解析
 * @author ganyu
 * @time 2017/7/21 17:38
 */
public class ObjectTypeAdapter implements JsonSerializer<Object>, JsonDeserializer<Object> {

    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        if (src.getClass() != Object.class) {
            return context.serialize(src, typeOfSrc);
        }
        return new JsonObject();
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonObject()) {
            return context.deserialize(json, typeOfT);
        }
        return null;
    }
}
