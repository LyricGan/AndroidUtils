package com.lyric.android.app.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author lyricgan
 * @time 2016/6/24 10:56
 */
public class ClazzUtils {

    public static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
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

    /**
     * 查找接口下的所有实现类
     * @param clazz 接口
     * @return 接口下的所有实现类
     */
    public static List<Class> getAllClassByInterface(Class<?> clazz) {
        List<Class> resultList = new ArrayList<>();
        if (clazz.isInterface()) {
            String packageName = clazz.getPackage().getName();
            try {
                List<Class> classList = getClasses(packageName);
                for (int i = 0; i < classList.size(); i++) {
                    Class<?> cls = classList.get(i);
                    if (clazz.isAssignableFrom(cls) && !clazz.equals(cls)) {
                        resultList.add(cls);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    /**
     * 从一个包中查找出所有的类，在jar包中不能查找
     * @param packageName 包名
     * @return 当前包下以及子包下的所有类
     * @throws ClassNotFoundException ClassNotFoundException
     * @throws IOException IOException
     */
    private static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (file.getName().contains(".")) {
                    continue;
                }
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
