package com.lyric.android.app.listener;

import android.view.View;
import android.widget.AbsListView;

/**
 * 列表滚动到底部事件监听
 * @author lyricgan
 * @time 2016/5/6 12:18
 */
public abstract class OnAbsListViewScrollListener implements AbsListView.OnScrollListener {
    private int mLastVisiblePosition = 0;
    private int mLastVisiblePositionY = 0;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                View childView = view.getChildAt(view.getChildCount() - 1);
                // 获取在屏幕内的绝对坐标
                int[] location = new int[2];
                childView.getLocationOnScreen(location);
                int childY = location[1];
                int lastVisiblePosition = view.getLastVisiblePosition();
                // 判断是否第一次滑动到底部
                if (lastVisiblePosition != mLastVisiblePosition && mLastVisiblePositionY != childY) {
                    mLastVisiblePosition = lastVisiblePosition;
                    mLastVisiblePositionY = childY;
                    return;
                }
                // 判断是否第二次滑动到底部
                if (lastVisiblePosition == mLastVisiblePosition && mLastVisiblePositionY == childY) {
                    onScrollToBottom(view, scrollState, lastVisiblePosition, childY);
                }
            }
            // 未滚动到底部，初始化
            mLastVisiblePosition = 0;
            mLastVisiblePositionY = 0;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public abstract void onScrollToBottom(AbsListView view, int scrollState, int lastVisiblePosition, int childY);
}
