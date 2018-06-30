package com.lyric.android.app.common;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * PagerAdapter基类
 * @author lyricgan
 */
public abstract class BasePagerAdapter extends PagerAdapter {
    private List<View> mViews;
    private List<String> mTitles;

    public BasePagerAdapter(List<View> views) {
        this(views, null);
    }

    public BasePagerAdapter(List<View> views, List<String> titles) {
        this.mViews = views;
        this.mTitles = titles;
    }

    @Override
    public int getCount() {
        return mViews != null ? mViews.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View itemView = mViews.get(position);
        container.removeView(itemView);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mViews.get(position);
        instantiateItem(itemView, container, position);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null) {
            if (position >= 0 && position < mTitles.size()) {
                return mTitles.get(position);
            }
        }
        return null;
    }

    /**
     * 初始化Item
     * @param itemView Item视图
     * @param container 容器视图
     * @param position Item索引位置
     */
    public abstract void instantiateItem(View itemView, ViewGroup container, int position);
}
