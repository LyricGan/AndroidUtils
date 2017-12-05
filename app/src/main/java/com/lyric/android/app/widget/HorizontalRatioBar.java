package com.lyric.android.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.utils.StringUtils;

/**
 * 自定义水平比例图，比例值可自适应文字
 * @author lyricgan
 * @date 2017/11/9 18:42
 */
public class HorizontalRatioBar extends View {
    private int mBackgroundColor;
    private float mTextSize;
    private int mTextColor;
    private boolean mShowText;
    private float mCornerRadius;

    private Paint mPaint;
    private Paint mTextPaint;
    /** 文字高度值一半 */
    private float mHalfTextHeight;
    /** 用于设置弧形的上下左右 */
    private RectF mOvalRectF;

    /** 比例值 */
    private float[] mRatios;
    /** 比例颜色值 */
    private int[] mRatioColors;
    /** 比例内容值 */
    private String[] mRadioTexts;

    public HorizontalRatioBar(Context context) {
        this(context, null);
    }

    public HorizontalRatioBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalRatioBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HorizontalRatioBar);
        try {
            mBackgroundColor = array.getColor(R.styleable.HorizontalRatioBar_hrb_backgroundColor, Color.GRAY);
            mTextSize = array.getDimension(R.styleable.HorizontalRatioBar_hrb_textSize, 0);
            mTextColor = array.getColor(R.styleable.HorizontalRatioBar_hrb_backgroundColor, Color.WHITE);
            mShowText = array.getBoolean(R.styleable.HorizontalRatioBar_hrb_showText, true);
            mCornerRadius = array.getDimension(R.styleable.HorizontalRatioBar_hrb_cornerRadius, 0);
        } finally {
            array.recycle();
        }
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);

        // 测量字体高度，用于文字居中展示
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mHalfTextHeight = Math.abs((fontMetrics.top + fontMetrics.bottom) / 2);

        mOvalRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isDataValid()) {
            return;
        }
        float viewWidth = getWidth() - mCornerRadius * 2;
        float viewHeight = getHeight();
        if (mShowText) {
            // 测量出文字需要的最小宽度
            float minTextWidth = 0;
            for (int i = 0; i < mRatios.length; i++) {
                String radioText = mRadioTexts[i];
                float textWidth = mTextPaint.measureText(radioText);
                if (minTextWidth > 0) {
                    minTextWidth = Math.max(textWidth, minTextWidth);
                } else {
                    minTextWidth = textWidth;
                }
            }
            adjustText(mRatios, minTextWidth / viewWidth);
        }
        // 绘制比例图背景
        mPaint.setColor(mBackgroundColor);
        canvas.drawRect(mCornerRadius, 0, viewWidth, viewHeight, mPaint);
        // 遍历比例值数组绘制比例图
        float lastX = 0;
        for (int i = 0; i < mRatios.length; i++) {
            mPaint.setColor(mRatioColors[i]);
            if (i == 0) {
                mOvalRectF.left = 0;
                mOvalRectF.right = mCornerRadius * 2;
                mOvalRectF.top = 0;
                mOvalRectF.bottom = viewHeight;
                canvas.drawArc(mOvalRectF, -90, -180, true, mPaint);
                lastX += mCornerRadius;
            }
            float width = viewWidth * mRatios[i];
            canvas.drawRect(lastX, 0, lastX + width, viewHeight, mPaint);
            if (mShowText) {
                String radioText = mRadioTexts[i];
                mTextPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(radioText, lastX + width / 2, viewHeight / 2 + mHalfTextHeight, mTextPaint);
            }
            if (i == mRatios.length - 1) {
                mOvalRectF.left = lastX + width - mCornerRadius;
                mOvalRectF.right = lastX + width + mCornerRadius;
                mOvalRectF.top = 0;
                mOvalRectF.bottom = viewHeight;
                canvas.drawArc(mOvalRectF, -90, 180, true, mPaint);
            }
            lastX = lastX + width;
        }
    }

    private boolean isDataValid() {
        if (mRatios != null && mRatioColors != null && mRadioTexts != null) {
            if (mRatios.length == mRatioColors.length && mRatios.length == mRadioTexts.length) {
                return true;
            }
        }
        return false;
    }

    public synchronized void setRatios(float[] ratios, int[] ratioColors, String[] radioTexts) {
        this.mRatios = ratios;
        this.mRatioColors = ratioColors;
        this.mRadioTexts = radioTexts;
        postInvalidate();
    }

    /**
     * 适应文字最小宽度
     * @param ratios 比例值，总和为1
     * @param minRadios 最小比例值，适应文字宽度
     */
    private void adjustText(float[] ratios, float minRadios) {
        float deltaRadios = 0;
        for (int i = 0; i < ratios.length; i++) {
            float ratio = ratios[i];
            if (ratio > 0 && ratio < minRadios) {
                ratios[i] = minRadios;
                deltaRadios += minRadios - ratio;
            }
        }
        if (deltaRadios > 0) {
            float totalRadio = 0;
            for (int i = 0; i < ratios.length; i++) {
                float ratio = ratios[i];
                if (ratio > minRadios) {
                    ratios[i] = (1 - deltaRadios) * ratio;
                }
                totalRadio += ratios[i];
                if (i == ratios.length - 1) {
                    ratios[i] = ratios[i] + (1 - totalRadio);
                }
            }
        }
    }

    public void test() {
        float homeRadio = 0.50f;
        float flatRadio = 0.20f;
        float awayRadio = 0.30f;
        float[] ratios = {homeRadio, flatRadio, awayRadio};
        int[] ratioColors = {0xffeb221f, 0xffb3c526, 0xff1fbbe9};
        String homeRadioString = "主胜" + StringUtils.formatDecimals(homeRadio * 100, 0, false) + "%";
        String flatRadioString = "平" + StringUtils.formatDecimals(flatRadio * 100, 0, false) + "%";
        String awayRadioString = "客胜" + StringUtils.formatDecimals(awayRadio * 100, 0, false) + "%";
        String[] radioTexts = {homeRadioString, flatRadioString, awayRadioString};
        setRatios(ratios, ratioColors, radioTexts);
    }
}
