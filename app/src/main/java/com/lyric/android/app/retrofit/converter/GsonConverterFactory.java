package com.lyric.android.app.retrofit.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author lyricgan
 * @description A {@linkplain Converter.Factory converter} which uses Gson for JSON.
 * @time 2016/7/28 11:13
 */
public class GsonConverterFactory extends Converter.Factory {
    private final Gson mGson;

    private GsonConverterFactory(Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        this.mGson = gson;
    }

    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code mGson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFactory create(Gson gson) {
        return new GsonConverterFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter<>(mGson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(mGson, adapter);
    }
}
