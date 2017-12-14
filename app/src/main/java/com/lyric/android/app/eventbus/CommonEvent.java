package com.lyric.android.app.eventbus;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 通用事件
 * @author lyricgan
 * @time 2017/2/17 14:59
 */
public class CommonEvent {
    /** 事件类型：刷新 */
    public static final int REFRESH = 0;

    @IntDef({REFRESH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventType{}

    /** 事件类型 */
    public @EventType int type;
    /** 事件ID */
    public String id;
    /** 事件对象 */
    public Object object;

    public CommonEvent(@EventType int type) {
        this.type = type;
    }

    public CommonEvent(@EventType int type, String id) {
        this.type = type;
        this.id = id;
    }

    public CommonEvent(@EventType int type, String id, Object object) {
        this.type = type;
        this.id = id;
        this.object = object;
    }
}
