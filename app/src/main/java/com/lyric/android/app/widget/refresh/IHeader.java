package com.lyric.android.app.widget.refresh;

import android.view.View;

public interface IHeader {

    View getView();

    /**
     * 下拉准备刷新动作
     * @param fraction  当前下拉高度与总高度的比
     * @param maxHeight 头部可拉伸最大高度
     * @param height    头部高度
     */
    void onPullingDown(float fraction, float maxHeight, float height);

    /**
     * 下拉释放过程
     * @param fraction  当前下拉高度与总高度的比
     * @param maxHeight 头部可拉伸最大高度
     * @param height    头部高度
     */
    void onPullReleasing(float fraction, float maxHeight, float height);

    void startAnimation(float maxHeight, float height);

    void onFinish(OnAnimaitonEndListener animEndListener);

    void reset();
}
