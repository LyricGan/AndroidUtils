package com.lyric.android.app.widget;

import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 视图缓存类，可对视图进行回收处理
 * @author lyricgan
 * @date 2017/12/4 13:22
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
        for(T t : views){
            mCacheList.add(new WeakReference<>(t));
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
        T itemView = null;
        if (mCacheList.size() > 0) {
            itemView = mCacheList.remove(0).get();
        }
        return itemView;
    }
}
