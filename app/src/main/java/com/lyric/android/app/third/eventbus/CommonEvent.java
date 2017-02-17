package com.lyric.android.app.third.eventbus;

/**
 * 通用事件
 * @author lyricgan
 * @time 2017/2/17 14:59
 */
public class CommonEvent implements CommonEventType {
    private int type;

    public CommonEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
