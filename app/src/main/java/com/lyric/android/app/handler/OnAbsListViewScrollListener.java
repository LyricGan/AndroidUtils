package com.lyric.android.app.handler;

import android.view.View;
import android.widget.AbsListView;

/**
 * @author lyric
 * @description AbsListView滚动到底部事件监听
 * @time 2016/5/6 12:18
 */
public class OnAbsListViewScrollListener implements AbsListView.OnScrollListener {
    private OnBottomCallback mCallback;
    private int mGetLastVisiblePosition = 0;
    private int mLastVisiblePositionY = 0;

    public interface OnBottomCallback {
        void callback();
    }

    public OnAbsListViewScrollListener(OnBottomCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                View v = view.getChildAt(view.getChildCount() - 1);
                int[] location = new int[2];
                v.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
                int y = location[1];
                if (view.getLastVisiblePosition() != mGetLastVisiblePosition && mLastVisiblePositionY != y) {// 第一次拖至底部
                    mGetLastVisiblePosition = view.getLastVisiblePosition();
                    mLastVisiblePositionY = y;
                    return;
                } else if (view.getLastVisiblePosition() == mGetLastVisiblePosition && mLastVisiblePositionY == y) {// 第二次拖至底部
                    if (mCallback != null) {
                        mCallback.callback();
                    }
                }
            }
            // 未滚动到底部，初始化
            mGetLastVisiblePosition = 0;
            mLastVisiblePositionY = 0;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }
}
