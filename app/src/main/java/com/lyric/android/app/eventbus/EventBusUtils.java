package com.lyric.android.app.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * register()与unregister()需配套使用
 * @author lyricgan
 * @time 2016/3/30 14:29
 */
public class EventBusUtils {

    private EventBusUtils() {
    }

    public static void register(Object subscriber) {
        if (!isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    public static void unregister(Object subscriber) {
        if (isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    public static boolean isRegistered(Object subscriber) {
        return EventBus.getDefault().isRegistered(subscriber);
    }

    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }

    public static void postSticky(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    public static void cancelEventDelivery(Object event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }

    public static <T> T getStickyEvent(Class<T> eventType) {
        return EventBus.getDefault().getStickyEvent(eventType);
    }

    public static void removeStickyEvent(Object event) {
        EventBus.getDefault().removeStickyEvent(event);
    }

    public static <T> T removeStickyEvent(Class<T> eventType) {
        return EventBus.getDefault().removeStickyEvent(eventType);
    }

    public static void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }

    public static boolean hasSubscriberForEvent(Class<?> eventClass) {
        return EventBus.getDefault().hasSubscriberForEvent(eventClass);
    }
}
