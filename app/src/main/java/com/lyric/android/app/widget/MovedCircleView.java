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

/**
 * @author lyric
 * @description
 * @time 2016/3/15 15:07
 */
public class MovedCircleView extends View {
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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(0xff007eff);
        if (mCurrentX == 0 || mCurrentY == 0) {
            canvas.drawCircle(mDefaultRadius * 3, mDefaultRadius * 5, mDefaultRadius, mPaint);
        } else {
            canvas.drawCircle(mCurrentX, mCurrentY, mDefaultRadius, mPaint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
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
                if (Math.abs(mCurrentX - mStartX) > 50 || Math.abs(mCurrentY - mStartY) > 50) {
                    invalidate();
                }
            }
                break;
            case MotionEvent.ACTION_UP: {
                mPaint.setColor(0xff007eff);
                invalidate();
            }
                break;
            case MotionEvent.ACTION_CANCEL: {
            }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
            }
                break;
            case MotionEvent.ACTION_MOVE: {
            }
                break;
            case MotionEvent.ACTION_UP: {
            }
                break;
            case MotionEvent.ACTION_CANCEL: {
            }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private class OnViewTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    }

}
