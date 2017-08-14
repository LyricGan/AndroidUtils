package com.lyric.android.app.widget.refresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DefaultFooterView extends View implements IFooter {
    public static final int DEFAULT_SIZE = 50;
    private float mCircleSpacing;
    private float[] scaleFloats = new float[]{1f, 1f, 1f};

    private ArrayList<ValueAnimator> mAnimators;
    private Map<ValueAnimator, ValueAnimator.AnimatorUpdateListener> mUpdateListeners = new HashMap<>();
    private Paint mPaint;
    private int mNormalColor = 0xffeeeeee;
    private int mAnimatingColor = 0xffe75946;

    public DefaultFooterView(Context context) {
        this(context, null);
    }

    public DefaultFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int defaultSize = RefreshUtils.dp2px(context, DEFAULT_SIZE);
        LayoutParams params = new LayoutParams(defaultSize, defaultSize, Gravity.CENTER);
        setLayoutParams(params);

        mCircleSpacing = RefreshUtils.dp2px(context, 4);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    public void setIndicatorColor(int color) {
        mPaint.setColor(color);
    }

    public void setNormalColor(@ColorInt int color) {
        mNormalColor = color;
    }

    public void setAnimatingColor(@ColorInt int color) {
        mAnimatingColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = (Math.min(getWidth(), getHeight()) - mCircleSpacing * 2) / 6;
        float x = getWidth() / 2 - (radius * 2 + mCircleSpacing);
        float y = getHeight() / 2;
        for (int i = 0; i < 3; i++) {
            canvas.save();
            float translateX = x + (radius * 2) * i + mCircleSpacing * i;
            canvas.translate(translateX, y);
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            canvas.drawCircle(0, 0, radius, mPaint);
            canvas.restore();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimators != null) {
            for (int i = 0; i < mAnimators.size(); i++) {
                mAnimators.get(i).cancel();
            }
        }
    }

    public void startAnimation() {
        if (mAnimators == null) {
            createAnimators();
        }
        if (mAnimators == null) {
            return;
        }
        if (isStarted()) {
            return;
        }
        for (int i = 0; i < mAnimators.size(); i++) {
            ValueAnimator animator = mAnimators.get(i);
            // when the animator restart, add the updateListener again because they was removed by animator stop.
            ValueAnimator.AnimatorUpdateListener updateListener = mUpdateListeners.get(animator);
            if (updateListener != null) {
                animator.addUpdateListener(updateListener);
            }
            animator.start();
        }
        setIndicatorColor(mAnimatingColor);
    }

    public void stopAnimation() {
        if (mAnimators != null) {
            for (ValueAnimator animator : mAnimators) {
                if (animator != null && animator.isStarted()) {
                    animator.removeAllUpdateListeners();
                    animator.end();
                }
            }
        }
        setIndicatorColor(mNormalColor);
    }

    private boolean isStarted() {
        for (ValueAnimator animator : mAnimators) {
            return animator.isStarted();
        }
        return false;
    }

    private void createAnimators() {
        mAnimators = new ArrayList<>();
        int[] delays = new int[]{120, 240, 360};
        for (int i = 0; i < 3; i++) {
            final int index = i;
            ValueAnimator scaleAnimator = ValueAnimator.ofFloat(1, 0.3f, 1);
            scaleAnimator.setDuration(750);
            scaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
            scaleAnimator.setStartDelay(delays[i]);
            mUpdateListeners.put(scaleAnimator, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            mAnimators.add(scaleAnimator);
        }
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingUp(float fraction, float maxHeadHeight, float headHeight) {
        stopAnimation();
    }

    @Override
    public void startAnimation(float maxHeadHeight, float headHeight) {
        startAnimation();
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float height) {
        stopAnimation();
    }

    @Override
    public void onFinish() {
        stopAnimation();
    }

    @Override
    public void reset() {
        stopAnimation();
    }
}
