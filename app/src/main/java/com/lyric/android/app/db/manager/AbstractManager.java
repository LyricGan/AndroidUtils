package com.lyric.android.app.db.manager;

import com.lyric.android.app.BaseApplication;
import com.lyric.android.app.db.DbHelper;
import com.lyric.android.app.entity.BaseEntity;

import io.realm.Realm;

/**
 * @author ganyu
 * @description 数据库本地管理类，抽象类、泛型。
 * @time 2016/1/21 17:21
 */
public abstract class AbstractManager<E extends BaseEntity> {
    protected Realm mRealm;

    public AbstractManager() {
        mRealm = DbHelper.getHelper(BaseApplication.getContext()).getRealm();
    }

    public abstract void add(E object);

    public abstract void delete();
}
