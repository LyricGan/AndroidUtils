package com.lyric.android.app.eventbus;

/**
 * @author lyricgan
 * @description register()与unregister()需配套使用。示例：
 * #EventBusUtils.register(this);
 * #EventBusUtils.unregister(this);
 * #EventBusUtils.post(int eventType);
 * #EventBusUtils.postSticky(int eventType);
 * @time 2016/3/31 13:09
 */
public class EventBusUtils extends BaseEventBusUtils {

    EventBusUtils() {
    }

    public static void post(int eventType) {
        post(EventBuilder.build(eventType));
    }

    public static void post(Object message) {
        if (message instanceof Integer) {
            return;
        }
        post(EventBuilder.build(0, message));
    }

    public static void post(int eventType, Object message) {
        post(EventBuilder.build(eventType, message));
    }

    public static void post(int eventType, Object message, int arg1) {
        post(EventBuilder.build(eventType, message, arg1));
    }

    public static void postSticky(int eventType) {
        postSticky(EventBuilder.build(eventType));
    }

    public static void postSticky(Object message) {
        if (message instanceof Integer) {
            return;
        }
        postSticky(EventBuilder.build(0, message));
    }

    public static void postSticky(int eventType, Object message) {
        postSticky(EventBuilder.build(eventType, message));
    }

    public static void postSticky(int eventType, Object message, int arg1) {
        postSticky(EventBuilder.build(eventType, message, arg1));
    }
}
