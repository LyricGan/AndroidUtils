package com.lyric.android.app.db;

import com.lyric.android.app.BaseApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author ganyu
 * @description 数据库帮助类
 * @time 2016/1/21 16:03
 */
public class DbHelper {
    private static final String DB_NAME = "db_utils";
    private static Realm mRealm;

    DbHelper() {
    }

    public synchronized static Realm getRealm() {
        if (mRealm == null) {
            mRealm = Realm.getInstance(new RealmConfiguration.Builder(BaseApplication.getContext()).name(DB_NAME).build());
        }
        return mRealm;
    }

    public String getDbName() {
        return DB_NAME;
    }
}
