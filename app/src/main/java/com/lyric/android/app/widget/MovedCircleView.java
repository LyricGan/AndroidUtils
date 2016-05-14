package com.lyric.android.app.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lyric.android.library.utils.DensityUtils;
import com.lyric.android.library.utils.LogUtils;

/**
 * @author lyric
 * @description
 * @time 2016/3/15 15:07
 */
public class MovedCircleView extends View implements View.OnTouchListener {
    private static final String TAG = MovedCircleView.class.getSimpleName();
    private TextPaint mPaint = new TextPaint();
    private int mDefaultRadius = 0;
    private float mStartX;
    private float mStartY;
    private float mCurrentX;
    private float mCurrentY;

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
        mDefaultRadius = DensityUtils.dip2px(context, 50);

        LogUtils.e(TAG, "mDefaultRadius:" + mDefaultRadius);
        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(0xff007eff);
        if (mCurrentX == 0 || mCurrentY == 0) {
            canvas.drawCircle(mDefaultRadius * 3.5f, mDefaultRadius * 5, mDefaultRadius, mPaint);
        } else {
            canvas.drawCircle(mCurrentX, mCurrentY, mDefaultRadius, mPaint);
        }
        canvas.restore();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mStartX = event.getX();
                mStartY = event.getY();
                LogUtils.e(TAG, "mStartX:" + mStartX + ",mStartY:" + mStartY);
                mPaint.setColor(0x88007eff);
                invalidate();
            }
                break;
            case MotionEvent.ACTION_MOVE: {
                mCurrentX = event.getX();
                mCurrentY = event.getY();
                LogUtils.e(TAG, "mCurrentX:" + mCurrentX + ",mCurrentY:" + mCurrentY);
                if (Math.abs(mCurrentX - mStartX) > 0 || Math.abs(mCurrentY - mStartY) > 0) {
                    invalidate();
                }
            }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
