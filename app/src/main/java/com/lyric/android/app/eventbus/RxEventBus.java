package com.lyric.android.app.eventbus;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * @author ganyu
 * @description Rx for event bus
 * @time 2016/1/25 17:08
 */
public class RxEventBus {
    private static final String TAG = RxEventBus.class.getSimpleName();
    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();
    private static RxEventBus sInstance;
    public static boolean DEBUG = false;

    private RxEventBus() {
    }

    public static synchronized RxEventBus get() {
        if (null == sInstance) {
            sInstance = new RxEventBus();
        }
        return sInstance;
    }

    public <T> Observable<T> register(@NonNull Object tag, @NonNull Class<T> clazz) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }
        Subject<T, T> subject;
        subjectList.add(subject = PublishSubject.create());
        if (DEBUG) {
            Log.d(TAG, "[register]subjectMapper: " + subjectMapper);
        }
        return subject;
    }

    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove((Subject) observable);
            if (subjects.size() > 0) {
                subjectMapper.remove(tag);
            }
        }
        if (DEBUG) {
            Log.d(TAG, "[unregister]subjectMapper: " + subjectMapper);
        }
    }

    public void post(@NonNull Object content) {
        post(content.getClass().getName(), content);
    }

    public void post(@NonNull Object tag, @NonNull Object content) {
        List<Subject> subjectList = subjectMapper.get(tag);

        if (subjectList != null && subjectList.size() > 0) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
        if (DEBUG) {
            Log.d(TAG, "[send]subjectMapper: " + subjectMapper);
        }
    }
}
