package com.lyric.android.app.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lyric.android.library.utils.DisplayUtils;
import com.lyric.android.library.utils.LogUtils;

/**
 * @author lyric
 * @description
 * @time 2016/3/15 15:07
 */
public class MovedCircleView extends View {
    private static final String TAG = MovedCircleView.class.getSimpleName();
    private TextPaint mPaint = new TextPaint();
    private int mDefaultRadius = 0;
    private float mStartX;
    private float mStartY;
    private float mCurrentX;
    private float mCurrentY;
    private float mDefaultPositionX;
    private float mDefaultPositionY;
    private int mMinDistance;

    public MovedCircleView(Context context) {
        this(context, null);
    }

    public MovedCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovedCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MovedCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        mDefaultRadius = DisplayUtils.dip2px(context, 64);
        mDefaultPositionX = DisplayUtils.getScreenWidth(context) * 0.5f;
        mDefaultPositionY = DisplayUtils.getScreenHeight(context) * 0.5f - mDefaultRadius * 0.8f;
        mMinDistance = mDefaultRadius / 16;

        LogUtils.e(TAG, "mDefaultRadius:" + mDefaultRadius + ",mDefaultPositionX:" + mDefaultPositionX + ",mDefaultPositionY:" + mDefaultPositionY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentX == 0 || mCurrentY == 0) {
            mPaint.setColor(0xff007eff);
            canvas.drawCircle(mDefaultPositionX, mDefaultPositionY, mDefaultRadius, mPaint);
        } else {
            canvas.drawCircle(mCurrentX, mCurrentY, mDefaultRadius, mPaint);
        }
        canvas.restore();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mStartX = event.getX();
                mStartY = event.getY();
                mPaint.setColor(0x88007eff);
                invalidate();
            }
                break;
            case MotionEvent.ACTION_MOVE: {
                mCurrentX = event.getX();
                mCurrentY = event.getY();
                if (Math.abs(mCurrentX - mStartX) > mMinDistance || Math.abs(mCurrentY - mStartY) > mMinDistance) {
                    mPaint.setColor(0x88007eff);
                    invalidate();
                }
            }
                break;
            case MotionEvent.ACTION_UP: {
                mPaint.setColor(0xff007eff);
                invalidate();
            }
                break;
            default:
                break;
        }
        return true;
    }
}
