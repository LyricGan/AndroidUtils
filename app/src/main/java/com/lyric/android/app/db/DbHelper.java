package com.lyric.android.app.db;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author ganyu
 * @description 数据库帮助类
 * @time 2016/1/21 16:03
 */
public class DbHelper {
    private static final String DB_NAME = "db_utils";
    private static DbHelper mInstance = new DbHelper();
    private static Realm mRealm;

    DbHelper() {
    }

    public synchronized static DbHelper getHelper(Context context) {
        if (mRealm == null) {
            mRealm = Realm.getInstance(new RealmConfiguration.Builder(context).name(DB_NAME).build());
        }
        return mInstance;
    }

    public Realm getRealm() {
        return mRealm;
    }

    public String getDbName() {
        return DB_NAME;
    }

    public String getPath() {
        if (mRealm == null) {
            throw new NullPointerException("Realm can not be null");
        }
        return mRealm.getPath();
    }

    public void close() {
        if (mRealm == null) {
            throw new NullPointerException("Realm can not be null");
        }
        mRealm.close();
    }
}
