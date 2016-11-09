package com.lyric.android.app.network;

import java.lang.reflect.Type;

/**
 * @author lyricgan
 * @description
 * @time 2016/8/5 14:05
 */
public interface Converter {

    <T> T convert(String json, Type type);
}
