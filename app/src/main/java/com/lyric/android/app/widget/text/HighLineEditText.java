package com.lyric.android.app.widget.text;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;

/**
 * 自定义高亮输入框
 *
 * @author Lyric Gan
 * @since 2017/3/29 18:17
 */
public class HighLineEditText extends EditText {
    private static final float ITEM_HEIGHT = 125;
    private boolean mReLayout = false;
    private OnTextWatcher mOnTextWatcher;

    public HighLineEditText(Context context) {
        this(context, null);
    }

    public HighLineEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HighLineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mOnTextWatcher != null) {
                    mOnTextWatcher.beforeTextChanged(s, start, count, after);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float add = ITEM_HEIGHT;
                setLineSpacing(0f, 1f);
                setLineSpacing(add, 0);
                setIncludeFontPadding(false);
                setGravity(Gravity.CENTER_VERTICAL);
                int top = (int) ((add - getTextSize()) * 0.5f);
                setPadding(getPaddingLeft(), top, getPaddingRight(), -top);
                if (mOnTextWatcher != null) {
                    mOnTextWatcher.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mOnTextWatcher != null) {
                    mOnTextWatcher.afterTextChanged(s);
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mReLayout) {
            mReLayout = true;
            setIncludeFontPadding(false);
            setGravity(Gravity.CENTER_VERTICAL);
            setLineSpacing(ITEM_HEIGHT, 0);
            int top = (int) ((ITEM_HEIGHT - getTextSize()) * 0.5f);
            setPadding(getPaddingLeft(), top, getPaddingRight(), -top);
            requestLayout();
            invalidate();
        }
    }

    public void addOnTextWatcher(OnTextWatcher textWatcher) {
        this.mOnTextWatcher = textWatcher;
    }

    public interface OnTextWatcher {

        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);
    }
}
