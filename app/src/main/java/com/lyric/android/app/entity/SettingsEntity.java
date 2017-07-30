package com.lyric.android.app.entity;

import android.app.Activity;

/**
 * 设置实体类
 * @author ganyu
 * @time 17/7/30
 */
public class SettingsEntity {
    private String name;
    private Class<? extends Activity> cls;

    public SettingsEntity(String name, Class<? extends Activity> cls) {
        this.name = name;
        this.cls = cls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<? extends Activity> getCls() {
        return cls;
    }

    public void setCls(Class<? extends Activity> cls) {
        this.cls = cls;
    }
}
