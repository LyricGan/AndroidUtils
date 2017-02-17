/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lyric.android.app.third.retrofit.converter;

import android.text.TextUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

final class DefaultRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        writer.write(encodeParams(getMap(value)));
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
    }

    public static <K, V> Map<K, V> getMap(Object object) {
        Map<K, V> result = new HashMap<K, V>();
        try {
            Method[] superMethods = object.getClass().getSuperclass().getDeclaredMethods();
            for (Method method : superMethods) {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    Object value = method.invoke(object, (Object[]) null);
                    result.put((K) field, (V) (null == value ? "" : value));
                }
            }
            Method[] methods = object.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().startsWith("get")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("get") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    Object value = method.invoke(object, (Object[]) null);
                    result.put((K) field, (V) (null == value ? "" : value));
                }
            }
            result.remove("class");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String encodeParams(Map<Object, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : params.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            if (TextUtils.isEmpty(value)) {
                builder.append(key).append("=").append(value).append("&");
            } else {
                builder.append(key).append("=").append(value).append("&");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
