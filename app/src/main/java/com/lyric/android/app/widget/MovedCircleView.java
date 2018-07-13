package com.lyric.android.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.lyric.android.app.utils.DisplayUtils;

/**
 * @author lyricgan
 */
public class MovedCircleView extends View {
    private static final int DEFAULT_COLOR = 0xff007eff;
    private static final int PRESSED_COLOR = 0x88007eff;

    private int mDefaultWidth;
    private int mDefaultHeight;
    private int mMinDistance;
    private TextPaint mPaint;

    private float mLastX;
    private float mLastY;
    private float mCurrentX;
    private float mCurrentY;

    public MovedCircleView(Context context) {
        this(context, null);
    }

    public MovedCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovedCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mDefaultWidth = DisplayUtils.dip2px(getContext(), 64);
        mDefaultHeight = DisplayUtils.dip2px(getContext(), 64);
        mMinDistance = DisplayUtils.dip2px(context, 16);

        mPaint = new TextPaint();
        mPaint.setColor(DEFAULT_COLOR);

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        // 处理wrap_content的情况
        if (widthMeasureSpecMode == MeasureSpec.AT_MOST && heightMeasureSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultWidth, mDefaultHeight);
        } else if (widthMeasureSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultWidth, heightMeasureSpecSize);
        } else if (heightMeasureSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSpecSize, mDefaultHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = getWidth() / 2;
        float cx = mCurrentX;
        float cy = mCurrentY;
        if (cx == 0 || cy == 0) {
            cx = cy = radius;
        }
        canvas.drawCircle(cx, cy, radius, mPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ViewParent viewParent = getParent();
                if (viewParent != null) {
                    viewParent.requestDisallowInterceptTouchEvent(true);
                }
                mLastX = event.getX();
                mLastY = event.getY();
                mPaint.setColor(PRESSED_COLOR);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                mCurrentY = event.getY();
                if (Math.abs(mCurrentX - mLastX) > mMinDistance || Math.abs(mCurrentY - mLastY) > mMinDistance) {
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mPaint.setColor(DEFAULT_COLOR);
                invalidate();
                if (Math.abs(event.getX() - mLastX) <= 3 && Math.abs(event.getY() - mLastY) <= 3) {
                    return performClick();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
