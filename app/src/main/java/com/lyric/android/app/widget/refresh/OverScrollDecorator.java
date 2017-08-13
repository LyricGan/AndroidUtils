package com.lyric.android.app.widget.refresh;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

public class OverScrollDecorator extends Decorator {
    // 主要为了监测Fling的动作,实现越界回弹
    private float mVelocityY;

    // 满足越界的手势的最低速度(默认3000)
    private static final int OVER_SCROLL_MIN_VX = 3000;

    // 针对View的延时策略
    private static final int MSG_START_COMPUTE_SCROLL = 0; // 开始计算
    private static final int MSG_CONTINUE_COMPUTE_SCROLL = 1;// 继续计算
    private static final int MSG_STOP_COMPUTE_SCROLL = 2; // 停止计算

    private int cur_delay_times = 0; //当前计算次数
    private static final int ALL_DELAY_TIMES = 60;  //10ms计算一次,总共计算20次

    public OverScrollDecorator(GraceRefreshLayout.CoreProcessor processor, IDecorator decorator) {
        super(processor, decorator);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return decorator != null && decorator.dispatchTouchEvent(ev);
    }

    @Override
    public boolean interceptTouchEvent(MotionEvent ev) {
        return decorator != null && decorator.interceptTouchEvent(ev);
    }

    @Override
    public boolean dealTouchEvent(MotionEvent e) {
        return decorator != null && decorator.dealTouchEvent(e);
    }

    private boolean preventTopOverScroll = false;
    private boolean preventBottomOverScroll = false;
    private boolean checkOverScroll = false;

    @Override
    public void onFingerDown(MotionEvent ev) {
        if (decorator != null) decorator.onFingerDown(ev);
        preventTopOverScroll = RefreshUtils.isViewToTop(coreProcessor.getTargetView(), coreProcessor.getTouchSlop());
        preventBottomOverScroll = RefreshUtils.isViewToBottom(coreProcessor.getTargetView(), coreProcessor.getTouchSlop());
    }

    @Override
    public void onFingerUp(MotionEvent ev, boolean isFling) {

        if (decorator != null) {
            decorator.onFingerUp(ev, checkOverScroll && isFling);
        }
        checkOverScroll = false;
    }

    @Override
    public void onFingerScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY, float velocityX, float velocityY) {
        if (decorator != null)
            decorator.onFingerScroll(e1, e2, distanceX, distanceY, velocityX, velocityY);
    }

    @Override
    public void onFingerFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (decorator != null) decorator.onFingerFling(e1, e2, velocityX, velocityY);
        //fling时才触发OverScroll，获取速度并采用演示策略估算View是否滚动到边界
        if (!coreProcessor.enableOverScroll()) return;

        int dy = (int) (e2.getY() - e1.getY());
        if (dy < -coreProcessor.getTouchSlop() && preventBottomOverScroll) return;//控件滚动在底部且向上fling
        if (dy > coreProcessor.getTouchSlop() && preventTopOverScroll) return;//控件滚动在顶部且向下fling

        mVelocityY = velocityY;
        if (Math.abs(mVelocityY) >= OVER_SCROLL_MIN_VX) {
            mHandler.sendEmptyMessage(MSG_START_COMPUTE_SCROLL);
            checkOverScroll = true;
        } else {
            mVelocityY = 0;
            cur_delay_times = ALL_DELAY_TIMES;
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int mTouchSlop = coreProcessor.getTouchSlop();
            switch (msg.what) {
                case MSG_START_COMPUTE_SCROLL:
                    cur_delay_times = -1; //这里没有break,写作-1方便计数
                case MSG_CONTINUE_COMPUTE_SCROLL:
                    cur_delay_times++;
                    View mChildView = coreProcessor.getTargetView();
                    if (coreProcessor.allowOverScroll()) {
                        if (mVelocityY >= OVER_SCROLL_MIN_VX) {
                            if (RefreshUtils.isViewToTop(mChildView, mTouchSlop)) {
                                coreProcessor.getAnimProcessor().animOverScrollTop(mVelocityY, cur_delay_times);
                                mVelocityY = 0;
                                cur_delay_times = ALL_DELAY_TIMES;
                            }
                        } else if (mVelocityY <= -OVER_SCROLL_MIN_VX) {
                            if (RefreshUtils.isViewToBottom(mChildView, mTouchSlop)) {
                                coreProcessor.getAnimProcessor().animOverScrollBottom(mVelocityY, cur_delay_times);
                                mVelocityY = 0;
                                cur_delay_times = ALL_DELAY_TIMES;
                            }
                        }
                    }
                    //计算未超时，继续发送消息并循环计算
                    if (cur_delay_times < ALL_DELAY_TIMES)
                        mHandler.sendEmptyMessageDelayed(MSG_CONTINUE_COMPUTE_SCROLL, 10);
                    break;
                case MSG_STOP_COMPUTE_SCROLL:
                    cur_delay_times = ALL_DELAY_TIMES;
                    break;
            }
        }
    };
}
