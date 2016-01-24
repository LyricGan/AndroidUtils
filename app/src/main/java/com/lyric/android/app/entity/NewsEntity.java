package com.lyric.android.app.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author ganyu
 * @description 资讯信息实体类
 * @time 2016/1/21 17:16
 */
public class NewsEntity extends Entity<RealmNewsEntity> {
    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("intro")
    @Expose
    public String intro;
    @SerializedName("add_time")
    @Expose
    public long addTime;
    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("source")
    @Expose
    public String source;
    @SerializedName("cover_url")
    @Expose
    public String coverUrl;
    @SerializedName("details_url")
    @Expose
    public String detailsUrl;

    @Override
    public RealmNewsEntity transformToRealm() {
        RealmNewsEntity realmNewsEntity = new RealmNewsEntity();
        realmNewsEntity.setId(id);
        realmNewsEntity.setTitle(title);
        realmNewsEntity.setIntro(intro);
        realmNewsEntity.setAddTime(addTime);
        realmNewsEntity.setAuthor(author);
        realmNewsEntity.setSource(source);
        realmNewsEntity.setCoverUrl(coverUrl);
        realmNewsEntity.setDetailsUrl(detailsUrl);
        return realmNewsEntity;
    }

    @Override
    public Entity transformFromRealm(RealmNewsEntity realm) {
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.id = realm.getId();
        newsEntity.title = realm.getTitle();
        newsEntity.intro = realm.getIntro();
        newsEntity.addTime = realm.getAddTime();
        newsEntity.author = realm.getAuthor();
        newsEntity.source = realm.getSource();
        newsEntity.coverUrl = realm.getCoverUrl();
        newsEntity.detailsUrl = realm.getDetailsUrl();
        return newsEntity;
    }

    @Override
    public Class getRealmClass() {
        return RealmNewsEntity.class;
    }
}
