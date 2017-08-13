package com.lyric.android.app.widget.refresh;

public abstract class PullListenerWrapper implements PullListener {

    @Override
    public void onPullingDown(GraceRefreshLayout refreshLayout, float fraction) {
    }

    @Override
    public void onPullingUp(GraceRefreshLayout refreshLayout, float fraction) {
    }

    @Override
    public void onPullDownReleasing(GraceRefreshLayout refreshLayout, float fraction) {
    }

    @Override
    public void onPullUpReleasing(GraceRefreshLayout refreshLayout, float fraction) {
    }

    @Override
    public void onRefresh(GraceRefreshLayout refreshLayout) {
    }

    @Override
    public void onLoadMore(GraceRefreshLayout refreshLayout) {
    }

    @Override
    public void onFinishRefresh() {

    }

    @Override
    public void onFinishLoadMore() {

    }

    @Override
    public void onRefreshCanceled() {

    }

    @Override
    public void onLoadMoreCanceled() {

    }
}