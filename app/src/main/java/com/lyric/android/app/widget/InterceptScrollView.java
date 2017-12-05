package com.lyric.android.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * 自定义ScrollView，拦截滑动事件
 * @author lyricgan
 * @time 2016/10/26 11:04
 */
public class InterceptScrollView extends ScrollView {
    private float mDownY;
    private int mTouchSlop;
    /** 是否拦截滑动标识 */
    private boolean mIntercept = false;

    public InterceptScrollView(Context context) {
        this(context, null);
    }

    public InterceptScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InterceptScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getRawY();
                // 判断滑动距离是否超过默认距离
                if (Math.abs(moveY - mDownY) > mTouchSlop && mIntercept) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setIntercept(boolean isIntercept) {
        this.mIntercept = isIntercept;
    }
}
