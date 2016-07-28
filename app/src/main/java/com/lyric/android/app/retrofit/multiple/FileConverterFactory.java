package com.lyric.android.app.retrofit.multiple;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/28 17:22
 */
public class FileConverterFactory extends Converter.Factory {

    public static FileConverterFactory create() {
        return new FileConverterFactory();
    }

    @Override
    public Converter<ResponseBody, File> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return FileResponseBodyConverter.INSTANCE;
    }
}
