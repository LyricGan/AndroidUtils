package com.lyric.android.app.data.entity;

import java.util.List;

/**
 * @author lyricgan
 * @time 2016/8/3 11:43
 */
public class BaseListWrapperEntity<T> {
    private int start;
    private int more;
    private List<T> data;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getMore() {
        return more;
    }

    public void setMore(int more) {
        this.more = more;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
