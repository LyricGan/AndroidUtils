package com.lyric.android.library.utils;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author lyric
 * @description
 * @time 2016/6/21 14:21
 */
public class ParameterizedTypeUtils {

    private ParameterizedTypeUtils() {
    }

    public static Type getType(final Type ownerType, final Class<?> declaredClass, int paramIndex) {
        Class<?> clazz;
        ParameterizedType parameterizedType;
        Type[] typeArray = null;
        TypeVariable<?>[] typeVariableArray = null;
        if (ownerType instanceof ParameterizedType) {
            parameterizedType = (ParameterizedType) ownerType;
            clazz = (Class<?>) parameterizedType.getRawType();
            typeArray = parameterizedType.getActualTypeArguments();
            typeVariableArray = clazz.getTypeParameters();
        } else {
            clazz = (Class<?>) ownerType;
        }
        if (declaredClass == clazz) {
            if (typeArray != null) {
                return typeArray[paramIndex];
            }
            return Object.class;
        }
        Type[] types = clazz.getGenericInterfaces();
        if (types != null) {
            for (int i = 0; i < types.length; i++) {
                Type t = types[i];
                if (t instanceof ParameterizedType) {
                    Class<?> cls = (Class<?>) ((ParameterizedType) t).getRawType();
                    if (declaredClass.isAssignableFrom(cls)) {
                        try {
                            return getReallyType(getType(t, declaredClass, paramIndex), typeVariableArray, typeArray);
                        } catch (Throwable ignored) {
                        }
                    }
                }
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            if (declaredClass.isAssignableFrom(superClass)) {
                return getReallyType(getType(clazz.getGenericSuperclass(), declaredClass, paramIndex), typeVariableArray, typeArray);
            }
        }
        throw new IllegalArgumentException("FindGenericType:" + ownerType + ", declaredClass: " + declaredClass + ", index: " + paramIndex);
    }

    private static Type getReallyType(Type type, TypeVariable<?>[] typeVariables, Type[] actualTypes) {
        if (type instanceof TypeVariable<?>) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            String name = typeVariable.getName();
            if (actualTypes != null) {
                for (int i = 0; i < typeVariables.length; i++) {
                    if (name.equals(typeVariables[i].getName())) {
                        return actualTypes[i];
                    }
                }
            }
            return typeVariable;
        } else if (type instanceof GenericArrayType) {
            Type ct = ((GenericArrayType) type).getGenericComponentType();
            if (ct instanceof Class<?>) {
                return Array.newInstance((Class<?>) ct, 0).getClass();
            }
        }
        return type;
    }
}
