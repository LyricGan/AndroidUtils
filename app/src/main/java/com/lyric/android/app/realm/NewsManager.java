package com.lyric.android.app.realm;

import com.lyric.android.app.base.BaseApp;
import com.lyric.android.app.realm.entity.RealmNewsEntity;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * @author ganyu
 * @description 资讯信息持久化数据管理类
 * @time 2016/1/21 17:18
 */
public class NewsManager extends AbstractManager<RealmNewsEntity> {
    private static NewsManager mInstance;

    private NewsManager() {
        super(BaseApp.getContext());
    }

    public static NewsManager getInstance() {
        if (mInstance == null) {
            mInstance = new NewsManager();
        }
        return mInstance;
    }

    @Override
    public void add(RealmNewsEntity newsEntity) {
        getRealm().beginTransaction();
        RealmNewsEntity entity = getRealm().createObject(RealmNewsEntity.class);
        entity.setId(newsEntity.getId());
        entity.setTitle(newsEntity.getTitle());
        entity.setIntro(newsEntity.getIntro());
        entity.setAddTime(newsEntity.getAddTime());
        entity.setAuthor(newsEntity.getAuthor());
        entity.setSource(newsEntity.getSource());
        entity.setCoverUrl(newsEntity.getCoverUrl());
        entity.setDetailsUrl(newsEntity.getDetailsUrl());
        getRealm().commitTransaction();
    }

    @Override
    public void delete(long id) {
        getRealm().beginTransaction();
        RealmResults<RealmNewsEntity> realmResults = getRealm().where(RealmNewsEntity.class).equalTo("id", id).findAll();
        realmResults.deleteAllFromRealm();
        getRealm().commitTransaction();
    }

    @Override
    public void delete() {
        getRealm().beginTransaction();
        RealmResults<RealmNewsEntity> realmResults = getRealm().where(RealmNewsEntity.class).findAll();
        realmResults.deleteAllFromRealm();
        getRealm().commitTransaction();
    }

    @Override
    public RealmNewsEntity query(long id) {
        getRealm().beginTransaction();
        RealmResults<RealmNewsEntity> realmResults = getRealm().where(RealmNewsEntity.class).equalTo("id", id).findAll();
        RealmNewsEntity realmNewsEntity = realmResults.first();
        getRealm().commitTransaction();

        return realmNewsEntity;
    }

    @Override
    public List<RealmNewsEntity> query() {
        getRealm().beginTransaction();
        RealmResults<RealmNewsEntity> realmResults = getRealm().where(RealmNewsEntity.class).findAll();
        List<RealmNewsEntity> realmNewsEntityList = new ArrayList<>();
        for (RealmNewsEntity newsEntity : realmResults) {
            realmNewsEntityList.add(newsEntity);
        }
        getRealm().commitTransaction();

        return realmNewsEntityList;
    }
}
