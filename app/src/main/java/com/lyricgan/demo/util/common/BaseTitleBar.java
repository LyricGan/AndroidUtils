package com.lyricgan.demo.util.common;

import android.view.View;

/**
 * base title bar
 *
 * @author Lyric Gan
 */
public class BaseTitleBar {
    private View mTitleView;

    public BaseTitleBar(View titleView) {
        this.mTitleView = titleView;
    }

    public View getTitleView() {
        return mTitleView;
    }

    public void setTitleView(View titleView) {
        this.mTitleView = titleView;
    }
}
