package com.lyric.android.app.third.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.ConstructorConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * gson解析帮助类
 * @author ganyu
 * @time 2017/7/10 10:34
 */
public class GsonHelper {
    private static Gson sGson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(String.class, new StringTypeAdapter())
                .registerTypeAdapter(int.class, new IntegerTypeAdapter())
                .registerTypeAdapter(Object.class, new ObjectTypeAdapter());
        try {
            Class builderClass = (Class) gsonBuilder.getClass();
            Field field = builderClass.getDeclaredField("instanceCreators");
            field.setAccessible(true);
            Map<Type, InstanceCreator<?>> creatorMap = (Map<Type, InstanceCreator<?>>) field.get(gsonBuilder);
            gsonBuilder.registerTypeAdapterFactory(new CollectionTypeAdapterFactory(new ConstructorConstructor(creatorMap)));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        sGson = gsonBuilder.create();
    }

    private GsonHelper() {
    }

    public static Gson getGson() {
        return sGson;
    }

    /**
     * 将Json字符串转为指定对象
     *
     * @param json json字符串
     * @param type 对象类型
     * @param <T>  对象类型
     * @return
     * @throws JsonSyntaxException
     */
    public static <T> T fromJson(String json, Class<T> type) throws JsonSyntaxException {
        return getGson().fromJson(json, type);
    }
}
