package com.lyric.android.app.third.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonSyntaxException;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.SqlDateTypeAdapter;
import com.google.gson.internal.bind.TimeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * gson解析帮助类
 * @author ganyu
 * @time 2017/7/10 10:34
 */
public class GsonHelper {
    private static Gson sGson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        try {
            Class builderClass = (Class) gsonBuilder.getClass();
            Field field = builderClass.getDeclaredField("instanceCreators");
            field.setAccessible(true);
            Map<Type, InstanceCreator<?>> creatorMap = (Map<Type, InstanceCreator<?>>) field.get(gsonBuilder);
            ConstructorConstructor constructorConstructor = new ConstructorConstructor(creatorMap);
            // 此处需要注意添加顺序
            gsonBuilder.registerTypeAdapterFactory(new ReflectiveTypeAdapterFactory(constructorConstructor));
            gsonBuilder.registerTypeAdapterFactory(new CollectionTypeAdapterFactory(constructorConstructor));
            gsonBuilder.registerTypeAdapterFactory(new MapTypeAdapterFactory(constructorConstructor, false));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // 添加默认的解析器
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.JSON_ELEMENT_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(ObjectTypeAdapter.FACTORY);
        // type adapters for basic platform types
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.STRING_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.INTEGER_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.BOOLEAN_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.BYTE_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.SHORT_FACTORY);
        TypeAdapter<Number> longAdapter = longAdapter(LongSerializationPolicy.DEFAULT);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(long.class, Long.class, longAdapter));
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(double.class, Double.class, doubleAdapter(false)));
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(float.class, Float.class, floatAdapter(false)));
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.NUMBER_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.ATOMIC_INTEGER_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.ATOMIC_BOOLEAN_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(AtomicLong.class, atomicLongAdapter(longAdapter)));
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(AtomicLongArray.class, atomicLongArrayAdapter(longAdapter)));
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.ATOMIC_INTEGER_ARRAY_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.CHARACTER_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.STRING_BUILDER_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.STRING_BUFFER_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.URL_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.URI_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.UUID_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.CURRENCY_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.LOCALE_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.INET_ADDRESS_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.BIT_SET_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(DateTypeAdapter.FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.CALENDAR_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TimeTypeAdapter.FACTORY);
        gsonBuilder.registerTypeAdapterFactory(SqlDateTypeAdapter.FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.TIMESTAMP_FACTORY);
        gsonBuilder.registerTypeAdapterFactory(ArrayTypeAdapter.FACTORY);
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.CLASS_FACTORY);
        // type adapters for composite and user-defined types
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.ENUM_FACTORY);

        // 添加自定义解析器
        gsonBuilder.registerTypeAdapter(String.class, new StringTypeAdapter());
        gsonBuilder.registerTypeAdapter(int.class, new IntegerTypeAdapter());

        sGson = gsonBuilder.create();
    }

    private GsonHelper() {
    }

    public static Gson getGson() {
        return sGson;
    }

    private static TypeAdapter<Number> longAdapter(LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Number read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return in.nextLong();
            }

            @Override
            public void write(JsonWriter out, Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                out.value(value.toString());
            }
        };
    }

    private static TypeAdapter<AtomicLong> atomicLongAdapter(final TypeAdapter<Number> longAdapter) {
        return new TypeAdapter<AtomicLong>() {
            @Override
            public void write(JsonWriter out, AtomicLong value) throws IOException {
                longAdapter.write(out, value.get());
            }

            @Override
            public AtomicLong read(JsonReader in) throws IOException {
                Number value = longAdapter.read(in);
                return new AtomicLong(value.longValue());
            }
        }.nullSafe();
    }

    private static TypeAdapter<AtomicLongArray> atomicLongArrayAdapter(final TypeAdapter<Number> longAdapter) {
        return new TypeAdapter<AtomicLongArray>() {
            @Override
            public void write(JsonWriter out, AtomicLongArray value) throws IOException {
                out.beginArray();
                for (int i = 0, length = value.length(); i < length; i++) {
                    longAdapter.write(out, value.get(i));
                }
                out.endArray();
            }

            @Override
            public AtomicLongArray read(JsonReader in) throws IOException {
                List<Long> list = new ArrayList<Long>();
                in.beginArray();
                while (in.hasNext()) {
                    long value = longAdapter.read(in).longValue();
                    list.add(value);
                }
                in.endArray();
                int length = list.size();
                AtomicLongArray array = new AtomicLongArray(length);
                for (int i = 0; i < length; ++i) {
                    array.set(i, list.get(i));
                }
                return array;
            }
        }.nullSafe();
    }

    private static TypeAdapter<Number> doubleAdapter(boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Double read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return in.nextDouble();
            }

            @Override
            public void write(JsonWriter out, Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                double doubleValue = value.doubleValue();
                checkValidFloatingPoint(doubleValue);
                out.value(value);
            }
        };
    }

    private static TypeAdapter<Number> floatAdapter(boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>() {
            @Override
            public Float read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.nextNull();
                    return null;
                }
                return (float) in.nextDouble();
            }

            @Override
            public void write(JsonWriter out, Number value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }
                float floatValue = value.floatValue();
                checkValidFloatingPoint(floatValue);
                out.value(value);
            }
        };
    }

    private static void checkValidFloatingPoint(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(value
                    + " is not a valid double value as per JSON specification. To override this"
                    + " behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        }
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
