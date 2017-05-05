package com.lyric.android.app.widget.text;

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
 * @author ganyu
 * @description 带清除功能的EditText
 * @time 2016/1/29 10:58
 */
public class ClearEditText extends AutoCompleteTextView implements View.OnFocusChangeListener, TextWatcher {
    private Drawable mClearDrawable;
    private boolean hasFocus;
    private EditTextFocusChangeListener mEditTextFocusChangeListener;

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
        if (mClearDrawable == null) {
            // 设置默认图片
            return;
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setCursorVisible(true);
        // 默认设置隐藏图标
        setClearIconVisible(false);

        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_UP == event.getAction()) {
            float x = event.getX();
            int width = getWidth();
            if (getCompoundDrawables()[2] != null) {
                boolean touch = x > (width - getTotalPaddingRight()) && (x < (width - getPaddingRight()));
                if (touch) {
                    this.setText("");
                }
            }
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
        if (mEditTextFocusChangeListener != null) {
            mEditTextFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    /**
     * 设置清除图标的显示与隐藏
     * @param visible View.VISIBLE,View.GONE
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

    public void setOnEditTextFocusChangeListener(EditTextFocusChangeListener editTextFocusChangeListener) {
        this.mEditTextFocusChangeListener = editTextFocusChangeListener;
    }

    /**
     * 设置默认晃动动画
     */
    public void setDefaultAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    /**
     * 左右晃动动画
     * @param counts 1秒钟晃动多少下
     * @return Animation
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
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
    public interface EditTextFocusChangeListener {

        void onFocusChange(View v, boolean hasFocus);
    }
}
