package com.lyric.android.app.widget;

import java.io.Serializable;

/**
 * @author lyricgan
 * @description
 * @time 2016/11/14 15:06
 */
public class ListSelectEntity implements Serializable {
    private int id;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
