package com.lyric.android.app.realm;

import com.lyric.android.app.base.BaseApplication;
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
        super(BaseApplication.getContext());
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
        if (realmResults != null) {
            realmResults.clear();
        }
        getRealm().commitTransaction();
    }

    @Override
    public void delete() {
        getRealm().beginTransaction();
        RealmResults<RealmNewsEntity> realmResults = getRealm().where(RealmNewsEntity.class).findAll();
        if (realmResults != null) {
            realmResults.clear();
        }
        getRealm().commitTransaction();
    }

    @Override
    public RealmNewsEntity query(long id) {
        getRealm().beginTransaction();
        RealmNewsEntity realmNewsEntity = null;
        RealmResults<RealmNewsEntity> realmResults = getRealm().where(RealmNewsEntity.class).equalTo("id", id).findAll();
        if (realmResults != null) {
            realmNewsEntity = realmResults.get(0);
        }
        getRealm().commitTransaction();

        return realmNewsEntity;
    }

    @Override
    public List<RealmNewsEntity> query() {
        getRealm().beginTransaction();
        List<RealmNewsEntity> realmNewsEntityList = null;
        RealmResults<RealmNewsEntity> realmResults = getRealm().where(RealmNewsEntity.class).findAll();
        if (realmResults != null) {
            realmNewsEntityList = new ArrayList<>();
            for (RealmNewsEntity realmResult : realmResults) {
                realmNewsEntityList.add(realmResult);
            }
        }
        getRealm().commitTransaction();

        return realmNewsEntityList;
    }
}
