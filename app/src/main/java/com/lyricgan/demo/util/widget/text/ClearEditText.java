package com.lyricgan.demo.util.widget.text;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AutoCompleteTextView;

/**
 * 带清除功能的EditText
 * @author Lyric Gan
 * @since 2016/1/29 10:58
 */
public class ClearEditText extends AutoCompleteTextView implements View.OnFocusChangeListener, TextWatcher {
    private Drawable mClearDrawable;
    private boolean hasFocus;
    private OnEditTextFocusChangeListener mOnEditTextFocusChangeListener;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable != null) {
            mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
            setCursorVisible(true);
            // 默认设置隐藏图标
            setClearIconVisible(false);

            setOnFocusChangeListener(this);
            addTextChangedListener(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                int width = getWidth();
                // 监听触摸事件，判断是否点击了清除图标
                if (mClearDrawable != null) {
                    boolean isClear = x > (width - getTotalPaddingRight()) && (x < (width - getPaddingRight()));
                    if (isClear) {
                        setText("");
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (mOnEditTextFocusChangeListener != null) {
            mOnEditTextFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    /**
     * 设置清除图标的显示与隐藏
     * @param visible true or false
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFocus) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public void setOnEditTextFocusChangeListener(OnEditTextFocusChangeListener listener) {
        this.mOnEditTextFocusChangeListener = listener;
    }

    /**
     * 添加左右晃动动画
     * @param counts 指定时间晃动多少次
     * @param durationMillis 指定时间
     */
    public void addAnimation(int counts, long durationMillis) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(durationMillis);
        setAnimation(translateAnimation);
    }

    /**
     * 设置输入框光标停留在文字后面
     */
    public void setSelectionEnd() {
        CharSequence text = this.getText();
        if (text != null) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    /**
     * 可删除按钮的焦点发生变化的回调
     */
    public interface OnEditTextFocusChangeListener {

        void onFocusChange(View v, boolean hasFocus);
    }
}
