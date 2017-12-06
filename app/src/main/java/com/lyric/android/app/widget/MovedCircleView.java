package com.lyric.android.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.lyric.utils.DisplayUtils;

/**
 * @author lyricgan
 * @time 2016/3/15 15:07
 */
public class MovedCircleView extends View {
    private static final String TAG = MovedCircleView.class.getSimpleName();
    private static final int DEFAULT_COLOR = 0xff007eff;
    private static final int PRESSED_COLOR = 0x88007eff;
    private TextPaint mPaint = new TextPaint();
    private float mLastX;
    private float mLastY;
    private float mCurrentX;
    private float mCurrentY;
    private int mMinDistance;

    public MovedCircleView(Context context) {
        this(context, null);
    }

    public MovedCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovedCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        mMinDistance = DisplayUtils.dip2px(context, 16);

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
            setMeasuredDimension(getDefaultWidth(), getDefaultHeight());
        } else if (widthMeasureSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(getDefaultWidth(), heightMeasureSpecSize);
        } else if (heightMeasureSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSpecSize, getDefaultHeight());
        }
    }

    private int getDefaultWidth() {
        return DisplayUtils.dip2px(getContext(), 64);
    }

    private int getDefaultHeight() {
        return DisplayUtils.dip2px(getContext(), 64);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = getWidth() / 2;
        if (mCurrentX == 0 || mCurrentY == 0) {
            mPaint.setColor(DEFAULT_COLOR);
            canvas.drawCircle(radius, radius, radius, mPaint);
        } else {
            canvas.drawCircle(mCurrentX, mCurrentY, radius, mPaint);
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ViewParent viewParent = getParent();
                if (viewParent != null) {
                    viewParent.requestDisallowInterceptTouchEvent(true);
                }
                mLastX = x;
                mLastY = y;
                mPaint.setColor(PRESSED_COLOR);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = x;
                mCurrentY = y;
                if (Math.abs(mCurrentX - mLastX) > mMinDistance || Math.abs(mCurrentY - mLastY) > mMinDistance) {
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mPaint.setColor(DEFAULT_COLOR);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }
}
