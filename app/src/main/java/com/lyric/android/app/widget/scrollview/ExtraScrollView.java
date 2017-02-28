package com.lyric.android.app.widget.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * @author lyricgan
 * @description 自定义ScrollView，拦截滑动事件
 * @time 2016/10/26 11:04
 */
public class ExtraScrollView extends ScrollView {
    private int downX;
    private int downY;
    private int mTouchSlop;

    public ExtraScrollView(Context context) {
        this(context, null);
    }

    public ExtraScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtraScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
            }
                break;
            case MotionEvent.ACTION_MOVE: {
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
                break;
            }
        }
        return super.onInterceptTouchEvent(e);
    }
}
