package com.lyric.android.app.db.manager;

import com.lyric.android.app.entity.UserBaseEntity;

import io.realm.RealmResults;

/**
 * @author ganyu
 * @description 用户本地数据管理类
 * @time 2016/1/21 16:42
 */
public class UserManager extends AbstractManager<UserBaseEntity> {

    public UserManager() {
        super();
    }

    @Override
    public void add(UserBaseEntity userBaseEntity) {
        mRealm.beginTransaction();

        UserBaseEntity entity = mRealm.createObject(UserBaseEntity.class);
        entity.setId(userBaseEntity.getId());
        entity.setToken(userBaseEntity.getToken());
        entity.setName(userBaseEntity.getName());
        entity.setAge(userBaseEntity.getAge());
        entity.setMobile(userBaseEntity.getMobile());

        mRealm.commitTransaction();
    }

    @Override
    public void delete() {
        mRealm.beginTransaction();

        RealmResults<UserBaseEntity> realmResults = mRealm.where(UserBaseEntity.class).findAll();
        if (realmResults != null) {
            realmResults.clear();
        }

        mRealm.commitTransaction();
    }
}
