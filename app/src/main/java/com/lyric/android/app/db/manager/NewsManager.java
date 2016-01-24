package com.lyric.android.app.db.manager;

import com.lyric.android.app.entity.RealmNewsEntity;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * @author ganyu
 * @description 资讯信息持久化数据管理类
 * @time 2016/1/21 17:18
 */
public class NewsManager extends AbstractManager<RealmNewsEntity> {

    public NewsManager() {
        super();
    }

    @Override
    public void add(RealmNewsEntity newsEntity) {
        mRealm.beginTransaction();
        RealmNewsEntity entity = mRealm.createObject(RealmNewsEntity.class);
        entity.setId(newsEntity.getId());
        entity.setTitle(newsEntity.getTitle());
        entity.setIntro(newsEntity.getIntro());
        entity.setAddTime(newsEntity.getAddTime());
        entity.setAuthor(newsEntity.getAuthor());
        entity.setSource(newsEntity.getSource());
        entity.setCoverUrl(newsEntity.getCoverUrl());
        entity.setDetailsUrl(newsEntity.getDetailsUrl());
        mRealm.commitTransaction();
    }

    @Override
    public void delete(long id) {
        mRealm.beginTransaction();
        RealmResults<RealmNewsEntity> realmResults = mRealm.where(RealmNewsEntity.class).equalTo("id", id).findAll();
        if (realmResults != null) {
            realmResults.clear();
        }
        mRealm.commitTransaction();
    }

    @Override
    public void delete() {
        mRealm.beginTransaction();
        RealmResults<RealmNewsEntity> realmResults = mRealm.where(RealmNewsEntity.class).findAll();
        if (realmResults != null) {
            realmResults.clear();
        }
        mRealm.commitTransaction();
    }

    @Override
    public RealmNewsEntity query(long id) {
        mRealm.beginTransaction();
        RealmNewsEntity realmNewsEntity = null;
        RealmResults<RealmNewsEntity> realmResults = mRealm.where(RealmNewsEntity.class).equalTo("id", id).findAll();
        if (realmResults != null) {
            realmNewsEntity = realmResults.get(0);
        }
        mRealm.commitTransaction();

        return realmNewsEntity;
    }

    @Override
    public List<RealmNewsEntity> query() {
        mRealm.beginTransaction();
        List<RealmNewsEntity> realmNewsEntityList = null;
        RealmResults<RealmNewsEntity> realmResults = mRealm.where(RealmNewsEntity.class).findAll();
        if (realmResults != null) {
            realmNewsEntityList = new ArrayList<>();
            for (RealmNewsEntity realmResult : realmResults) {
                realmNewsEntityList.add(realmResult);
            }
        }
        mRealm.commitTransaction();

        return realmNewsEntityList;
    }
}
