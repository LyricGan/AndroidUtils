package com.lyric.android.app.widget.refresh;

import android.view.View;

public interface IFooter {

    View getView();

    /**
     * 上拉准备加载更多的动作
     * @param fraction  上拉高度与总高度之比
     * @param maxHeight 底部可拉伸最大高度
     * @param height    底部高度
     */
    void onPullingUp(float fraction, float maxHeight, float height);

    void startAnimation(float maxHeight, float height);

    /**
     * 上拉释放过程
     */
    void onPullReleasing(float fraction, float maxHeight, float height);

    void onFinish();

    void reset();
}
