package com.lyric.android.app.eventbus;

/**
 * @author lyricgan
 * @description 示例：EventBuilder.build(int)
 * @time 2016/3/31 11:20
 */
public class EventBuilder {

    EventBuilder() {
    }

    public static Event build(int eventType) {
        return build(eventType, null, 0);
    }

    public static <T> Event<T> build(int eventType, T message) {
        return build(eventType, message, 0);
    }

    public static <T> Event<T> build(int eventType, T message, int arg1) {
        return new Event<T>(eventType, message, arg1);
    }
}
