package com.lyric.android.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.lyric.android.app.R;

/**
 * 左右对阵图，分为左右两部分，可自定义颜色，监听视图变化。添加了线程同步，为线程安全控件
 * @author Lyric Gan
 * @since 2017/10/16 10:02
 */
public class ClashBar extends View {
    private static final int DEFAULT_LEFT_COLOR = Color.RED;
    private static final int DEFAULT_RIGHT_COLOR = Color.GREEN;
    /** 数据更新间隔次数 */
    private static final int DEFAULT_INTERVAL_TIMES = 30;
    /** 数据更新间隔时间 */
    private static final long DEFAULT_UPDATE_INTERVAL_TIME = 10L;

    private Paint mPaint;
    private int mLeftColor;
    private int mRightColor;

    private float mLeftData;
    private float mRightData;
    private float mTotalData;

    private float mLeftProgressData;
    private float mRightProgressData;
    private float mLeftDeltaData;
    private float mRightDeltaData;

    private OnClashBarUpdatedListener mOnClashBarUpdatedListener;

    public ClashBar(Context context) {
        this(context, null);
    }

    public ClashBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClashBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClashBar);
        try {
            mLeftColor = array.getColor(R.styleable.ClashBar_clash_bar_left_color, DEFAULT_LEFT_COLOR);
            mRightColor = array.getColor(R.styleable.ClashBar_clash_bar_right_color, DEFAULT_RIGHT_COLOR);
        } finally {
            array.recycle();
        }

        initPaints();
    }

    private void initPaints() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTotalData <= 0) {
            return;
        }
        int width = getWidth();
        int height = getHeight();
        mPaint.setColor(mLeftColor);
        canvas.drawRect(0, 0, mLeftProgressData / mTotalData * width, height, mPaint);
        mPaint.setColor(mRightColor);
        canvas.drawRect(width - (mRightProgressData / mTotalData * width), 0, width, height, mPaint);
    }

    public void setData(float leftData, float rightData) {
        setData(leftData, rightData, false);
    }

    public void setData(float leftData, float rightData, boolean isForceRefresh) {
        setData(leftData, rightData, isForceRefresh, DEFAULT_INTERVAL_TIMES);
    }

    public synchronized void setData(float leftData, float rightData, boolean isForceRefresh, int intervalTimes) {
        if (leftData < 0 || rightData < 0 || (leftData + rightData < 0)) {
            return;
        }
        // 判断是否强制刷新，如果不是则判断数据是否一致，如果一致则不刷新视图
        if (mLeftData == leftData && mRightData == rightData) {
            if (!isForceRefresh) {
                return;
            }
        }
        mLeftData = leftData;
        mRightData = rightData;
        mTotalData = leftData + rightData;
        mLeftProgressData = 0;
        mRightProgressData = 0;
        mLeftDeltaData = mLeftData / intervalTimes;
        mRightDeltaData = mRightData / intervalTimes;

        update(mLeftProgressData, mRightProgressData, false);
    }

    private void update(float leftProgressData, float rightProgressData, boolean isFinished) {
        mLeftProgressData = leftProgressData;
        mRightProgressData = rightProgressData;
        postInvalidate();

        if (mOnClashBarUpdatedListener != null) {
            mOnClashBarUpdatedListener.onChanged(mLeftData, mRightData, leftProgressData, rightProgressData, isFinished);
        }
        if (isFinished) {
            return;
        }
        postDelayed(mUpdateAction, DEFAULT_UPDATE_INTERVAL_TIME);
    }

    private Runnable mUpdateAction = new Runnable() {
        @Override
        public void run() {
            updateData(mLeftProgressData + mLeftDeltaData, mRightProgressData + mRightDeltaData);
        }
    };

    private void updateData(float leftProgressData, float rightProgressData) {
        boolean isFinished = (leftProgressData >= mLeftData && rightProgressData >= mRightData);
        if (isFinished) {
            leftProgressData = mLeftData;
            rightProgressData = mRightData;
        }
        update(leftProgressData, rightProgressData, isFinished);
    }

    public void setOnClashBarUpdatedListener(OnClashBarUpdatedListener listener) {
        this.mOnClashBarUpdatedListener = listener;
    }

    /**
     * 视图变化进度监听器
     */
    public interface OnClashBarUpdatedListener {

        void onChanged(float leftData, float rightData, float leftProgressData, float rightProgressData, boolean isFinished);
    }
}
