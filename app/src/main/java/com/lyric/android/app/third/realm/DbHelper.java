package com.lyric.android.app.third.realm;

import android.content.Context;

import io.realm.Realm;

/**
 * @author ganyu
 * @description 数据库帮助类
 * @time 2016/1/21 16:03
 */
public class DbHelper {
    private static final String DB_NAME = "db_utils";
    private static Realm mRealm;

    private DbHelper() {
    }

    public synchronized static Realm getRealm(Context context) {
        if (mRealm == null) {
//            mRealm = Realm.getInstance(new RealmConfiguration.Builder(context).name(DB_NAME).build());
        }
        return mRealm;
    }

    public static String getName() {
        return DB_NAME;
    }
}
