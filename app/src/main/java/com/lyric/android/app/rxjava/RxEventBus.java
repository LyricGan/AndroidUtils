package com.lyric.android.app.rxjava;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * @author lyricgan
 * @description Rx for event bus.
 * @time 2016/1/25 17:59
 */
public class RxEventBus {
    private static final String TAG = RxEventBus.class.getSimpleName();
    private static final boolean DEBUG = false;
    private ConcurrentHashMap<Object, List<Subject>> mSubjectMapper = new ConcurrentHashMap<>();
    private static RxEventBus sInstance;

    private RxEventBus() {
    }

    public static synchronized RxEventBus get() {
        if (null == sInstance) {
            sInstance = new RxEventBus();
        }
        return sInstance;
    }

    public <T> Observable<T> register(Object tag, Class<T> clazz) {
        if (DEBUG) {
            Log.d(TAG, "[register]mSubjectMapper: " + mSubjectMapper);
        }
        List<Subject> subjectList = mSubjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            mSubjectMapper.put(tag, subjectList);
        }
        Subject<T, T> subject;
        subjectList.add(subject = PublishSubject.create());
        return subject;
    }

    public void unregister(Object tag, Observable observable) {
        if (DEBUG) {
            Log.d(TAG, "[unregister]mSubjectMapper: " + mSubjectMapper);
        }
        List<Subject> subjects = mSubjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove((Subject) observable);
            if (subjects.size() > 0) {
                mSubjectMapper.remove(tag);
            }
        }
    }

    public boolean isRegister(Object tag, Observable observable) {
        if (mSubjectMapper.isEmpty()) {
            return false;
        }
        List<Subject> subjects = mSubjectMapper.get(tag);
        if (subjects == null || subjects.isEmpty()) {
            return false;
        }
        for (Subject subject : subjects) {
            if (observable == subject) {
                return true;
            }
        }
        return false;
    }

    public void post(Object content) {
        post(content.getClass().getName(), content);
    }

    public void post(Object tag, Object content) {
        if (DEBUG) {
            Log.d(TAG, "[send]mSubjectMapper: " + mSubjectMapper);
        }
        List<Subject> subjectList = mSubjectMapper.get(tag);
        if (subjectList != null && subjectList.size() > 0) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
    }
}
