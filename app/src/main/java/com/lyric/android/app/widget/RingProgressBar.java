package com.lyric.android.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.lyric.android.app.R;

/**
 * 圆环进度条，包括圆环背景和进度条
 * @author lyricgan
 * @date 2017/8/23 15:52
 */
public class RingProgressBar extends View {
    private static final int MAX_PROGRESS = 100;
    private static final String _PERCENT_V = "%";

    private Paint mRingPaint;
    /** 圆环的颜色 */
    private int mRingColor;
    /** 圆环进度的颜色 */
    private int mRingProgressColor;
    /** 渐变进度条 */
    private SweepGradient mRingSweepGradient;
    /** 渐变颜色值 */
    private int[] mGradientColors;
    /** 文本画笔 */
    private Paint mTextPaint;
    private Paint mSmallTextPaint;
    /** 中间进度百分比的字符串的颜色 */
    private int mTextColor;
    private int mSmallTextColor;
    /** 中间进度的字体大小 */
    private float mTextSize;
    private float mSmallTextSize;

    /** 文本高度一半 */
    private float mHalfTextHeight;
    /** 是否显示文本 */
    private boolean mIsShowText;
    /** 是否显示文本 */
    private boolean mIsShowSmallText;

    private String mProgressText;
    private String mSmallText = _PERCENT_V;

    /** 外面设置进来的文本 */
    private String mText;
    /** 圆环的宽度 */
    private float mRingWidth;
    /** 最大进度 */
    private float mMaxProgress = MAX_PROGRESS;
    /** 实际进度 */
    private float mProgress = 0;
    /** 当前进度 */
    private float mCurrentProgress = 0;
    /** 用于定义的圆弧的形状和大小的界限 */
    private RectF mOvalRectF;

    private CountDownTimer mAnimationTimer;
    private float mAnimationDeltaProgress;
    /** 是否一直显示动画标识，不区分数据变化 */
    private boolean mAlwaysShowAnimation;

    public RingProgressBar(Context context) {
        this(context, null);
    }

    public RingProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressBar);
        try {
            mRingWidth = typedArray.getDimension(R.styleable.RingProgressBar_rpb_ringWidth, 0);
            mRingColor = typedArray.getColor(R.styleable.RingProgressBar_rpb_ringColor, Color.GRAY);
            mRingProgressColor = typedArray.getColor(R.styleable.RingProgressBar_rpb_ringProgressColor, Color.GREEN);
            mText = typedArray.getString(R.styleable.RingProgressBar_rpb_text);
            mTextSize = typedArray.getDimension(R.styleable.RingProgressBar_rpb_textSize, 0);
            mTextColor = typedArray.getColor(R.styleable.RingProgressBar_rpb_textColor, Color.GREEN);
            mSmallText = typedArray.getString(R.styleable.RingProgressBar_rpb_smallText);
            mSmallTextSize = typedArray.getDimension(R.styleable.RingProgressBar_rpb_smallTextSize, 0);
            mSmallTextColor = typedArray.getColor(R.styleable.RingProgressBar_rpb_smallTextColor, Color.GREEN);
            mIsShowText = typedArray.getBoolean(R.styleable.RingProgressBar_rpb_isShowText, true);
            mIsShowSmallText = typedArray.getBoolean(R.styleable.RingProgressBar_rpb_isShowSmallText, true);
            mCurrentProgress = typedArray.getFloat(R.styleable.RingProgressBar_rpb_progress, 0);
            mMaxProgress = typedArray.getFloat(R.styleable.RingProgressBar_rpb_maxProgress, MAX_PROGRESS);
            mAlwaysShowAnimation = typedArray.getBoolean(R.styleable.RingProgressBar_rpb_alwaysShowAnimation, false);
        } finally {
            typedArray.recycle();
        }
        initPaints();

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mHalfTextHeight = Math.abs((fm.bottom + fm.top) / 2);

        mOvalRectF = new RectF();

        initProgressText();
    }

    private void initPaints() {
        mRingPaint = new Paint();
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mRingWidth);
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);

        mSmallTextPaint = new Paint();
        mSmallTextPaint.setStyle(Paint.Style.STROKE);
        mSmallTextPaint.setAntiAlias(true);
        mSmallTextPaint.setTextSize(mSmallTextSize);
        mSmallTextPaint.setColor(mSmallTextColor);
    }

    private void initProgressText() {
        if (mMaxProgress > 0) {
            int percent = (int) ((mCurrentProgress / mMaxProgress) * 100);
            mProgressText = percent + "";
        } else {
            mProgressText = "";
        }
        if (TextUtils.isEmpty(mSmallText)) {
            mSmallText = _PERCENT_V;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制圆形背景
        int centre = getWidth() / 2;
        float radius = centre - mRingWidth / 2;
        mRingPaint.setColor(mRingColor);
        mRingPaint.setShader(null);
        canvas.drawCircle(centre, centre, radius, mRingPaint);

        if (mCurrentProgress <= 0) {
            return;
        }
        drawText(canvas, centre);

        // 绘制圆弧，圆环的进度
        mOvalRectF.left = centre - radius;
        mOvalRectF.top = centre - radius;
        mOvalRectF.right = centre + radius;
        mOvalRectF.bottom = centre + radius;
        mRingPaint.setStrokeWidth(mRingWidth + 0.6f);
        if (mRingSweepGradient == null) {
            if (mGradientColors != null && mGradientColors.length > 0) {
                if (mGradientColors.length == 1) {
                    mRingPaint.setColor(mGradientColors[0]);
                    mRingPaint.setShader(null);
                } else {
                    initRingSweepGradient(getWidth() / 2, getHeight() / 2, mGradientColors);
                    mRingPaint.setShader(mRingSweepGradient);
                }
            } else {
                mRingPaint.setColor(mRingProgressColor);
                mRingPaint.setShader(null);
            }
        } else {
            mRingPaint.setShader(mRingSweepGradient);
        }

        // 绘制路径时包含了线帽，要去掉对应的角度
        float capAngle = (float) ((mRingWidth / 2) / (2 * Math.PI * radius) * 360);
        float fromAngle = -90 + capAngle;
        float sweepAngle = 360 * mCurrentProgress / mMaxProgress - 2 * capAngle;
        if (mCurrentProgress == mMaxProgress) {
            sweepAngle = 360 * mCurrentProgress / mMaxProgress;
        }
        // 根据进度绘制圆弧
        if (sweepAngle != 0) {
            canvas.drawArc(mOvalRectF, fromAngle, sweepAngle, false, mRingPaint);
        }
        mRingPaint.setStrokeWidth(mRingWidth);
    }

    private void drawText(Canvas canvas, int centre) {
        if (!mIsShowText) {
            return;
        }
        String text;
        if (!TextUtils.isEmpty(mText)) {
            text = mText;
        } else {
            text = mProgressText;
        }
        if (!TextUtils.isEmpty(text)) {
            // 测量字体宽度，需要根据字体的宽度设置在圆环中间
            float textWidth = mTextPaint.measureText(text);
            if (mIsShowSmallText && !TextUtils.isEmpty(mSmallText)) {
                float smallTextWidth = mSmallTextPaint.measureText(mSmallText);

                canvas.drawText(text, centre - (textWidth + smallTextWidth) / 2, centre + mHalfTextHeight, mTextPaint);
                canvas.drawText(mSmallText, centre + (textWidth - smallTextWidth) / 2, centre + mHalfTextHeight, mSmallTextPaint);
            } else {
                canvas.drawText(text, centre - textWidth / 2, centre + mHalfTextHeight, mTextPaint);
            }
        }
    }

    private void initRingSweepGradient(float cx, float cy, int[] gradientColors) {
        mRingSweepGradient = new SweepGradient(cx, cy, gradientColors, null);
    }

    public float getRingWidth() {
        return mRingWidth;
    }

    public void setRingWidth(float ringWidth) {
        this.mRingWidth = ringWidth;
        mRingPaint.setStrokeWidth(ringWidth);
    }

    public int getRingColor() {
        return mRingColor;
    }

    public void setRingColor(int ringColor) {
        this.mRingColor = ringColor;
    }

    public int getProgressColor() {
        return mRingProgressColor;
    }

    public void setProgressColor(int progressColor) {
        this.mRingProgressColor = progressColor;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
    }

    public String getSmallText() {
        return mSmallText;
    }

    public void setSmallText(String smallText) {
        this.mSmallText = smallText;
    }

    public int getSmallTextColor() {
        return mSmallTextColor;
    }

    public void setSmallTextColor(int smallTextColor) {
        this.mSmallTextColor = smallTextColor;
    }

    public float getSmallTextSize() {
        return mSmallTextSize;
    }

    public void setSmallTextSize(float smallTextSize) {
        this.mSmallTextSize = smallTextSize;
    }

    public int[] getGradientColors() {
        return mGradientColors;
    }

    public void setSweepGradientColors(int[] colors) {
        this.mGradientColors = colors;
    }

    public boolean isAlwaysShowAnimation() {
        return mAlwaysShowAnimation;
    }

    public void setAlwaysShowAnimation(boolean alwaysShowAnimation) {
        this.mAlwaysShowAnimation = alwaysShowAnimation;
    }

    public synchronized float getMaxProgress() {
        return mMaxProgress;
    }

    public synchronized void setMaxProgress(float maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public synchronized float getProgress() {
        return mCurrentProgress;
    }

    public void setProgress(float progress) {
        setProgress(progress, MAX_PROGRESS);
    }

    /**
     * 设置进度
     * @param progress 进度
     * @param maxProgress 最大进度，默认为100
     */
    public synchronized void setProgress(float progress, float maxProgress) {
        if (progress < 0 || maxProgress <= 0) {
            return;
        }
        // 数据未变化则不刷新视图
        if (progress == mProgress && mMaxProgress == maxProgress) {
            if (!mAlwaysShowAnimation) {
                return;
            }
        }
        mMaxProgress = maxProgress;
        if (progress > mMaxProgress) {
            progress = mMaxProgress;
        }
        mProgress = progress;
        mCurrentProgress = 0;

        post(new Runnable() {
            @Override
            public void run() {
                startTimer();
            }
        });
    }

    /**
     * 更新进度
     * @param progress 进度
     */
    private synchronized void updateProgress(float progress) {
        if (progress < 0 || progress > mMaxProgress) {
            return;
        }
        mCurrentProgress = progress;
        initProgressText();
        postInvalidate();
    }

    private void startTimer() {
        // 实现动画绘制的倒计时
        final int totalTimerMillis = 1500;
        // 计时器间隙
        final int countDownTimerMillis = 10;
        if (mAnimationTimer == null) {
            mAnimationTimer = new CountDownTimer(totalTimerMillis, countDownTimerMillis) {

                @Override
                public void onFinish() {
                    updateProgress(mProgress);
                }

                @Override
                public void onTick(long millisUntilFinished) {
                    float currentProgress = mCurrentProgress + mAnimationDeltaProgress;
                    boolean isFinished = currentProgress >= mProgress;
                    if (isFinished) {
                        updateProgress(mProgress);
                        cancelTimer();
                        return;
                    }
                    updateProgress(currentProgress);
                }
            };
        }
        mAnimationDeltaProgress = mProgress / ((totalTimerMillis - 1000) / countDownTimerMillis);
        mAnimationTimer.cancel();
        mAnimationTimer.start();
    }

    private void cancelTimer() {
        if (mAnimationTimer != null) {
            mAnimationTimer.cancel();
        }
    }
}
