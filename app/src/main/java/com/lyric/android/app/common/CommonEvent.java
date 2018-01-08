package com.lyric.android.app.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 事件实体
 * @author lyricgan
 */
public class CommonEvent {
    @IntDef({
            EventType.REFRESH,
            EventType.LOGIN,
            EventType.LOGOUT
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface CommonEventType {
    }

    /** 事件类型 */
    public @CommonEventType
    int type;
    /** 事件ID */
    public String id;
    /** 事件对象 */
    public Object object;

    public CommonEvent(@CommonEventType int type) {
        this.type = type;
    }

    public CommonEvent(@CommonEventType int type, String id) {
        this.type = type;
        this.id = id;
    }

    public CommonEvent(@CommonEventType int type, String id, Object object) {
        this.type = type;
        this.id = id;
        this.object = object;
    }

    /**
     * 事件类型
     */
    public interface EventType {
        /** 刷新 */
        int REFRESH = 0;
        /** 登录 */
        int LOGIN = 1;
        /** 登出 */
        int LOGOUT = 2;
    }
}
