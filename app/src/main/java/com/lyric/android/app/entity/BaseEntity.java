package com.lyric.android.app.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author ganyu
 * @description 基础实体类
 * @time 2016/1/21 15:47
 */
public class BaseEntity extends RealmObject {
    @PrimaryKey
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
