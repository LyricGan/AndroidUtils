package com.lyric.android.app.widget.refresh;

public interface IPullListener {

    /**
     * 下拉中
     * @param refreshLayout
     * @param fraction
     */
    void onPullingDown(GraceRefreshLayout refreshLayout, float fraction);

    /**
     * 上拉
     */
    void onPullingUp(GraceRefreshLayout refreshLayout, float fraction);

    /**
     * 下拉松开
     * @param refreshLayout
     * @param fraction
     */
    void onPullDownReleasing(GraceRefreshLayout refreshLayout, float fraction);

    /**
     * 上拉松开
     */
    void onPullUpReleasing(GraceRefreshLayout refreshLayout, float fraction);

    /**
     * 刷新中
     */
    void onRefresh(GraceRefreshLayout refreshLayout);

    /**
     * 加载更多中
     */
    void onLoadMore(GraceRefreshLayout refreshLayout);

    void onFinishRefresh();

    void onFinishLoadMore();

    /**
     * 正在刷新时向上滑动屏幕，刷新被取消
     */
    void onRefreshCanceled();

    /**
     * 正在加载更多时向下滑动屏幕，加载更多被取消
     */
    void onLoadMoreCanceled();
}