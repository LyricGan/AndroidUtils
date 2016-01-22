package com.lyric.android.app.db.manager;

import com.lyric.android.app.entity.NewsEntity;

import io.realm.RealmResults;

/**
 * @author ganyu
 * @description 资讯信息持久化数据管理类
 * @time 2016/1/21 17:18
 */
public class NewsManager extends AbstractManager<NewsEntity> {

    public NewsManager() {
        super();
    }

    @Override
    public void add(NewsEntity newsEntity) {
        mRealm.beginTransaction();

        NewsEntity entity = mRealm.createObject(NewsEntity.class);
        entity.setId(newsEntity.getId());
        entity.setTitle(newsEntity.getTitle());
        entity.setIntro(newsEntity.getIntro());
        entity.setAddTime(newsEntity.getAddTime());
        entity.setSource(newsEntity.getSource());
        entity.setCoverUrl(newsEntity.getCoverUrl());
        entity.setDetailsUrl(newsEntity.getDetailsUrl());

        mRealm.commitTransaction();
    }

    @Override
    public void delete() {
        mRealm.beginTransaction();

        RealmResults<NewsEntity> realmResults = mRealm.where(NewsEntity.class).findAll();
        if (realmResults != null) {
            realmResults.clear();
        }

        mRealm.commitTransaction();
    }
}
