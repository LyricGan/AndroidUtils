package com.lyric.android.app.entity;

/**
 * @author ganyu
 * @description 实体类，抽象类、泛型，用来转换实体类
 * @time 16/1/24 上午11:40
 */
public abstract class Entity<T> {

    public abstract T transformToRealm();

    public abstract Entity transformFromRealm(T realm);

    public abstract Class getRealmClass();
}
