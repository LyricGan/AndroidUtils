package com.lyric.android.app.db.manager;

import com.lyric.android.app.db.DbHelper;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * @author ganyu
 * @description 本地持久化数据管理类，抽象类、泛型。
 * @time 2016/1/21 17:21
 */
public abstract class AbstractManager<E extends RealmObject> {
    protected Realm mRealm;

    public AbstractManager() {
        mRealm = DbHelper.getRealm();
    }

    public abstract void add(E object);

    public abstract void delete();
}
