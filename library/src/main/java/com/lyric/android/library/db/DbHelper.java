package com.lyric.android.library.db;

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
    private static Realm mRealm;

    DbHelper() {
    }

    public synchronized static Realm getRealm(Context context) {
        if (mRealm == null) {
            mRealm = Realm.getInstance(new RealmConfiguration.Builder(context).name(DB_NAME).build());
        }
        return mRealm;
    }

    public String getName() {
        return DB_NAME;
    }
}
