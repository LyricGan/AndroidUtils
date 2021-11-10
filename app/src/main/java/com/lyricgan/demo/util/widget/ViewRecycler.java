package com.lyricgan.demo.util.widget;

import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 视图缓存类，将视图添加到缓存列表，取视图时移除引用，回收内存
 * @author Lyric Gan
 * @since 2017/12/4 13:22
 */
public class ViewRecycler<T extends View> {
    private List<WeakReference<T>> mCacheList;

    public ViewRecycler() {
        mCacheList = new ArrayList<>();
    }

    public void cacheView(T view) {
        mCacheList.add(new WeakReference<>(view));
    }

    public void cacheViews(List<T> views) {
        for(T view : views){
            cacheView(view);
        }
    }

    public void cacheViews(ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            cacheView((T) viewGroup.getChildAt(i));
        }
    }

    public T getCacheView() {
        T view = null;
        if (mCacheList.size() > 0) {
            view = mCacheList.remove(0).get();
        }
        return view;
    }
}
