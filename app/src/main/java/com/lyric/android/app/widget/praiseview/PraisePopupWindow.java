package com.lyric.android.app.widget.praiseview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 带点赞效果的视图弹窗
 *
 * @author Lyric Gan
 */
public class PraisePopupWindow extends PopupWindow {
    /** 默认移动距离 */
    private static final int DEFAULT_DISTANCE = 60;
    /** Y轴移动起始偏移量 */
    private static final int DEFAULT_FROM_Y_DELTA = 0;
    /** Y轴移动最终偏移量 */
    private static final int DEFAULT_TO_Y_DELTA = DEFAULT_DISTANCE;
    /** 起始时透明度 */
    private static final float DEFAULT_FROM_ALPHA = 1.0f;
    /** 结束时透明度 */
    private static final float DEFAULT_TO_ALPHA = 0.0f;
    /** 动画时长 */
    private static final int DEFAULT_DURATION = 800;
    /** 默认文本 */
    private static final String DEFAULT_TEXT = "";
    /** 默认文本字体大小 */
    private static final int DEFAULT_TEXT_SIZE = 16;
    /** 默认文本字体颜色 */
    private static final int DEFAULT_TEXT_COLOR = Color.RED;

    private String mText = DEFAULT_TEXT;
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextSize = DEFAULT_TEXT_SIZE;
    private int mFromY = DEFAULT_FROM_Y_DELTA;
    private int mToY = DEFAULT_TO_Y_DELTA;
    private float mFromAlpha = DEFAULT_FROM_ALPHA;
    private float mToAlpha = DEFAULT_TO_ALPHA;
    private int mDuration = DEFAULT_DURATION;
    private int mDistance = DEFAULT_DISTANCE;

    private AnimationSet mAnimationSet;
    private boolean mChanged = false;
    private Context mContext = null;
    private TextView mTextView = null;

    public PraisePopupWindow(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        RelativeLayout layout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        mTextView = new TextView(mContext);
        mTextView.setIncludeFontPadding(false);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTextSize);
        mTextView.setTextColor(mTextColor);
        mTextView.setText(mText);
        mTextView.setLayoutParams(params);
        layout.addView(mTextView);
        setContentView(layout);

        int textViewWidth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int textViewHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mTextView.measure(textViewWidth, textViewHeight);
        setWidth(mTextView.getMeasuredWidth());
        setHeight(mDistance + mTextView.getMeasuredHeight());
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(false);
        setTouchable(false);
        setOutsideTouchable(false);

        mAnimationSet = getDefaultAnimation();
    }

    /**
     * 设置文本
     *
     * @param text
     */
    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            throw new IllegalArgumentException("text cannot be null.");
        }
        mText = text;
        mTextView.setText(text);
        mTextView.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int w = (int) mTextView.getPaint().measureText(text);
        setWidth(w);
        setHeight(mDistance + getTextViewHeight(mTextView, w));
    }

    private static int getTextViewHeight(TextView textView, int width) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }

    /**
     * 设置文本颜色
     *
     * @param color
     */
    private void setTextColor(int color) {
        mTextColor = color;
        mTextView.setTextColor(color);
    }

    /**
     * 设置文本大小
     *
     * @param textSize
     */
    private void setTextSize(int textSize) {
        mTextSize = textSize;
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
    }

    /**
     * 设置文本信息
     *
     * @param text
     * @param textColor
     * @param textSize
     */
    public void setTextInfo(String text, int textColor, int textSize) {
        setTextColor(textColor);
        setTextSize(textSize);
        setText(text);
    }

    /**
     * 设置图片
     *
     * @param resId
     */
    public void setImage(int resId) {
        setImage(mContext.getResources().getDrawable(resId));
    }

    /**
     * 设置图片
     *
     * @param drawable
     */
    public void setImage(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("drawable cannot be null.");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTextView.setBackground(drawable);
        } else {
            mTextView.setBackgroundDrawable(drawable);
        }
        mTextView.setText("");
        setWidth(drawable.getIntrinsicWidth());
        setHeight(mDistance + drawable.getIntrinsicHeight());
    }

    /**
     * 设置移动距离
     *
     * @param dis
     */
    public void setDistance(int dis) {
        mDistance = dis;
        mToY = dis;
        mChanged = true;
        setHeight(mDistance + mTextView.getMeasuredHeight());
    }

    /**
     * 设置Y轴移动属性
     *
     * @param fromY
     * @param toY
     */
    public void setTranslateY(int fromY, int toY) {
        mFromY = fromY;
        mToY = toY;
        mChanged = true;
    }

    /**
     * 设置透明度属性
     *
     * @param fromAlpha
     * @param toAlpha
     */
    public void setAlpha(float fromAlpha, float toAlpha) {
        mFromAlpha = fromAlpha;
        mToAlpha = toAlpha;
        mChanged = true;
    }

    /**
     * 设置动画时长
     *
     * @param duration
     */
    public void setDuration(int duration) {
        mDuration = duration;
        mChanged = true;
    }

    /**
     * 重置属性
     */
    public void reset() {
        mText = DEFAULT_TEXT;
        mTextColor = DEFAULT_TEXT_COLOR;
        mTextSize = DEFAULT_TEXT_SIZE;
        mFromY = DEFAULT_FROM_Y_DELTA;
        mToY = DEFAULT_TO_Y_DELTA;
        mFromAlpha = DEFAULT_FROM_ALPHA;
        mToAlpha = DEFAULT_TO_ALPHA;
        mDuration = DEFAULT_DURATION;
        mDistance = DEFAULT_DISTANCE;
        mChanged = false;
        mAnimationSet = getDefaultAnimation();
    }

    /**
     * 展示
     *
     * @param v
     */
    public void show(View v) {
        if (!isShowing()) {
            int offsetY = -v.getHeight() - getHeight();
            showAsDropDown(v, v.getWidth() / 2 - getWidth() / 2, offsetY);
            if (mAnimationSet == null || mChanged) {
                mAnimationSet = getDefaultAnimation();
                mChanged = false;
            }
            mTextView.startAnimation(mAnimationSet);
        }
    }

    private AnimationSet getDefaultAnimation() {
        // 平移动画
        TranslateAnimation translateAnim = new TranslateAnimation(0, 0, mFromY, -mToY);
        // 透明度渐变动画
        AlphaAnimation alphaAnim = new AlphaAnimation(mFromAlpha, mToAlpha);
        mAnimationSet = new AnimationSet(true);
        mAnimationSet.addAnimation(translateAnim);
        mAnimationSet.addAnimation(alphaAnim);
        mAnimationSet.setDuration(mDuration);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isShowing()) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    });
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return mAnimationSet;
    }
}
