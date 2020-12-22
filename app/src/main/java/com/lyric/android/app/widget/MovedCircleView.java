package com.lyric.android.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.utils.DisplayUtils;

/**
 * @author Lyric Gan
 */
public class MovedCircleView extends View {
    private static final int DEFAULT_COLOR = 0xff007eff;
    private static final int PRESSED_COLOR = 0x88007eff;

    private float mRadius;
    private int mColor;
    private int mPressedColor;
    private int mDefaultWidth;
    private int mDefaultHeight;
    private TextPaint mPaint;

    private float mLastX;
    private float mLastY;
    private float mCurrentX;
    private float mCurrentY;

    private int mLocationY;
    private int mScreenWidth;
    private int mScreenHeight;

    public MovedCircleView(Context context) {
        this(context, null);
    }

    public MovedCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovedCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDefaultWidth = DisplayUtils.dip2px(context, 64);
        mDefaultHeight = DisplayUtils.dip2px(context, 64);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MovedCircleView);
        try {
            mRadius = array.getDimension(R.styleable.MovedCircleView_mcv_radius, mDefaultWidth);
            mColor = array.getColor(R.styleable.MovedCircleView_mcv_color, DEFAULT_COLOR);
            mPressedColor = array.getColor(R.styleable.MovedCircleView_mcv_colorPressed, PRESSED_COLOR);
        } finally {
            array.recycle();
        }
        init(context);
    }

    private void init(Context context) {
        mPaint = new TextPaint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);

        mScreenWidth = DisplayUtils.getScreenWidth(context);
        mScreenHeight = DisplayUtils.getScreenHeight(context);

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
        int radius = (int) mRadius;
        float cx = mCurrentX;
        float cy = mCurrentY;
        if (mLocationY == 0) {
            int[] outLocation = new int[2];
            getLocationInWindow(outLocation);
            mLocationY = outLocation[1];
        }
        if (cx == 0 || cy == 0) {
            mCurrentX = cx = radius;
            mCurrentY = cy = radius;
        } else {
            if (cx - radius < 0) {
                mCurrentX = cx = radius;
            } else if (cx + radius > mScreenWidth) {
                mCurrentX = cx = mScreenWidth - radius;
            }
            if (cy - radius < 0) {
                mCurrentY = cy = radius;
            } else if (cy + radius + mLocationY > mScreenHeight) {
                mCurrentY = cy = mScreenHeight - radius - mLocationY;
            }
        }
        canvas.drawCircle(cx, cy, radius, mPaint);
        canvas.save();
        canvas.restore();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        if (isMoveDisabled(eventX, eventY)) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                mLastX = eventX;
                mLastY = eventY;

                mPaint.setColor(mPressedColor);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(eventX - mLastX) > 3 || Math.abs(eventY - mLastY) > 3) {
                    mCurrentX = eventX;
                    mCurrentY = eventY;
                    invalidate();

                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mPaint.setColor(mColor);
                invalidate();
                if (Math.abs(eventX - mLastX) <= 3 && Math.abs(eventY - mLastY) <= 3) {
                    return performClick();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isMoveDisabled(float eventX, float eventY) {
        return (eventX < mCurrentX - mRadius || eventX > mCurrentX + mRadius)
                || (eventY < mCurrentY - mRadius || eventY > mCurrentY + mRadius);
    }

}
