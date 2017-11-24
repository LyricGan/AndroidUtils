package com.lyric.android.app.eventbus;

/**
 * 通用事件
 * @author lyricgan
 * @time 2017/2/17 14:59
 */
public class CommonEvent {
    /** 事件类型 */
    public int type;
    /** 事件ID */
    public String id;
    /** 事件对象 */
    public Object object;

    public CommonEvent(int type) {
        this.type = type;
    }

    public CommonEvent(int type, String id) {
        this.type = type;
        this.id = id;
    }

    public CommonEvent(int type, String id, Object object) {
        this.type = type;
        this.id = id;
        this.object = object;
    }
}
