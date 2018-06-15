package com.lyric.android.app.common;

import android.view.View;

/**
 * 标题栏基类
 * @author lyricgan
 * @date 17/12/17 上午11:05
 */
public class BaseTitleBar {
    private View mTitleView;

    public BaseTitleBar(View titleView) {
        this.mTitleView = titleView;
    }

    public void setVisibility(int visibility) {
        mTitleView.setVisibility(visibility);
    }

    public View getTitleView() {
        return mTitleView;
    }

    public void setTitleView(View titleView) {
        this.mTitleView = titleView;
    }
}
