package com.lyric.android.app.widget;

import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 视图缓存
 */
public class ViewRecycler<T extends View> {
    private final List<WeakReference<T>> mCacheList = new ArrayList<>();

    public void cacheView(T view) {
        mCacheList.add(new WeakReference<T>(view));
    }

    public void cacheViews(List<T> views) {
        for(T t : views){
            mCacheList.add(new WeakReference<T>(t));
        }
    }

    public void cacheViews(ViewGroup viewGroup) {
        if (null != viewGroup) {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                cacheView((T) viewGroup.getChildAt(i));
            }
        }
    }

    public T getCacheView() {
        T item = null;
        if (mCacheList.size() > 0) {
            item = mCacheList.remove(0).get();
        }
        return item;
    }
}
