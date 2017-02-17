package com.lyric.android.app.third.realm;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * @author ganyu
 * @description 本地持久化数据管理类，抽象类、泛型。
 * @time 2016/1/21 17:21
 */
public abstract class AbstractManager<E extends RealmObject> {
    private Realm mRealm;

    public AbstractManager(Context context) {
        mRealm = DbHelper.getRealm(context);
    }

    public Realm getRealm() {
        return mRealm;
    }

    public abstract void add(E object);

    public abstract void delete(long id);

    public abstract void delete();

    public abstract E query(long id);

    public abstract List<E> query();
}
