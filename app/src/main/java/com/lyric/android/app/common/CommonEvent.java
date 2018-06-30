package com.lyric.android.app.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * common event
 *
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

    public @CommonEventType int type;
    public String id;
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
     * event type
     */
    public interface EventType {
        int REFRESH = 0;
        int LOGIN = 1;
        int LOGOUT = 2;
    }
}
