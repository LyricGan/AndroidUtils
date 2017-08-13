package com.lyric.android.app.widget.refresh;

import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class RefreshProcessor implements IDecorator {
    protected GraceRefreshLayout.CoreProcessor coreProcessor;
    private float mTouchX, mTouchY;
    private boolean intercepted = false;
    private boolean willAnimationHeader = false;
    private boolean willAnimationFooter = false;
    private boolean downEventSent = false;

    public RefreshProcessor(GraceRefreshLayout.CoreProcessor processor) {
        if (processor == null) {
            throw new NullPointerException("The coreProcessor can not be null.");
        }
        coreProcessor = processor;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downEventSent = false;
                intercepted = false;
                mTouchX = ev.getX();
                mTouchY = ev.getY();
                if (coreProcessor.isEnableKeepIView()) {
                    if (!coreProcessor.isRefreshing()) {
                        coreProcessor.setPrepareFinishRefresh(false);
                    }
                    if (!coreProcessor.isLoadingMore()) {
                        coreProcessor.setPrepareFinishLoadMore(false);
                    }
                }
                coreProcessor.dispatchTouchEventSuper(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                mLastMoveEvent = ev;
                float dx = ev.getX() - mTouchX;
                float dy = ev.getY() - mTouchY;
                if (!intercepted && Math.abs(dx) <= Math.abs(dy) && Math.abs(dy) > coreProcessor.getTouchSlop()) {//滑动允许最大角度为45度
                    if (dy > 0 && RefreshUtils.isViewToTop(coreProcessor.getTargetView(), coreProcessor.getTouchSlop()) && coreProcessor.allowPullDown()) {
                        coreProcessor.setStatePTD();
                        mTouchX = ev.getX();
                        mTouchY = ev.getY();
                        sendCancelEvent();
                        intercepted = true;
                        return true;
                    } else if (dy < 0 && RefreshUtils.isViewToBottom(coreProcessor.getTargetView(), coreProcessor.getTouchSlop()) && coreProcessor.allowPullUp()) {
                        coreProcessor.setStatePBU();
                        mTouchX = ev.getX();
                        mTouchY = ev.getY();
                        intercepted = true;
                        sendCancelEvent();
                        return true;
                    }
                }
                if (intercepted) {
                    if (coreProcessor.isRefreshVisible() || coreProcessor.isLoadingVisible()) {
                        return coreProcessor.dispatchTouchEventSuper(ev);
                    }
                    if (!coreProcessor.isPrepareFinishRefresh() && coreProcessor.isStatePTD()) {
                        if (dy < -coreProcessor.getTouchSlop() || !RefreshUtils.isViewToTop(coreProcessor.getTargetView(), coreProcessor.getTouchSlop())) {
                            coreProcessor.dispatchTouchEventSuper(ev);
                        }
                        dy = Math.min(coreProcessor.getMaxHeadHeight() * 2, dy);
                        dy = Math.max(0, dy);
                        coreProcessor.getAnimProcessor().scrollHeaderByMove(dy);
                    } else if (!coreProcessor.isPrepareFinishLoadMore() && coreProcessor.isStatePBU()) {
                        //加载更多的动作
                        if (dy > coreProcessor.getTouchSlop() || !RefreshUtils.isViewToBottom(coreProcessor.getTargetView(), coreProcessor.getTouchSlop())) {
                            coreProcessor.dispatchTouchEventSuper(ev);
                        }
                        dy = Math.max(-coreProcessor.getMaxBottomHeight() * 2, dy);
                        dy = Math.min(0, dy);
                        coreProcessor.getAnimProcessor().scrollFooterByMove(Math.abs(dy));
                    }
                    if (dy == 0 && !downEventSent) {
                        downEventSent = true;
                        sendDownEvent();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (intercepted) {
                    if (coreProcessor.isStatePTD()) {
                        willAnimationHeader = true;
                    } else if (coreProcessor.isStatePBU()) {
                        willAnimationFooter = true;
                    }
                    intercepted = false;
                    return true;
                }
                break;
        }
        return coreProcessor.dispatchTouchEventSuper(ev);
    }

    private MotionEvent mLastMoveEvent;

    // 发送cancel事件解决selection问题
    private void sendCancelEvent() {
        if (mLastMoveEvent == null) {
            return;
        }
        MotionEvent last = mLastMoveEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());
        coreProcessor.dispatchTouchEventSuper(e);
    }

    private void sendDownEvent() {
        final MotionEvent last = mLastMoveEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime(), MotionEvent.ACTION_DOWN, last.getX(), last.getY(), last.getMetaState());
        coreProcessor.dispatchTouchEventSuper(e);
    }

    @Override
    public boolean interceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean dealTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onFingerDown(MotionEvent ev) {
    }

    @Override
    public void onFingerUp(MotionEvent ev, boolean isFling) {
        if (!isFling && willAnimationHeader) {
            coreProcessor.getAnimProcessor().dealPullDownRelease();
        }
        if (!isFling && willAnimationFooter) {
            coreProcessor.getAnimProcessor().dealPullUpRelease();
        }
        willAnimationHeader = false;
        willAnimationFooter = false;
    }

    @Override
    public void onFingerScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY, float velocityX, float velocityY) {
        // 手指在屏幕上滚动，如果此时正处在刷新状态，可隐藏
        int mTouchSlop = coreProcessor.getTouchSlop();
        if (coreProcessor.isRefreshVisible() && distanceY >= mTouchSlop && !coreProcessor.isOpenFloatRefresh()) {
            coreProcessor.getAnimProcessor().animationHeaderHideByVy((int) velocityY);
        }
        if (coreProcessor.isLoadingVisible() && distanceY <= -mTouchSlop) {
            coreProcessor.getAnimProcessor().animationFooterHideByVy((int) velocityY);
        }
    }

    @Override
    public void onFingerFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    }
}
