package com.lyric.android.app.test.convert;

import java.util.LinkedHashMap;

/**
 * @author lyricgan
 * @description use String-Class as key-value for cache convert
 * @time 2016/10/20 15:59
 */
public enum Converter {
    instance;

    private static LinkedHashMap<String, Class<?>> mMap;

    static  {
        mMap = new LinkedHashMap<>();
        mMap.put("https://api.github.com/user", User.class);
        mMap.put("https://api.github.com/repository", Repository.class);
    }

    public LinkedHashMap<String, Class<?>> getMap() {
        return mMap;
    }

    public <T> T convert(String key) {
        Class<?> cls = getMap().get(key);
        try {
            // must implements default constructor (that is, zero-argument)
            return (T) cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void test() {
        User user = Converter.instance.convert("https://api.github.com/user");
        Repository repository = Converter.instance.convert("https://api.github.com/repository");
        if (user != null) {
            System.out.println(user.toString());
        }
        if (repository != null) {
            System.out.println(repository.toString());
        }
    }
}
