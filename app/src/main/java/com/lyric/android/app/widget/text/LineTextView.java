package com.lyric.android.app.widget.text;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;

/**
 * 自定义文本控件，控制TextView行高
 * @author lyricgan
 * @time 2017/3/29 18:17
 */
public class LineTextView extends EditText {
    private static final float ITEM_HEIGHT = 125;
    private boolean mReLayout = false;
    private TextWatcher mTextWatcher;

    public LineTextView(Context context) {
        super(context);
    }

    public LineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mTextWatcher != null) {
                    mTextWatcher.beforeTextChanged(s, start, count, after);
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
                if (mTextWatcher != null) {
                    mTextWatcher.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mTextWatcher != null) {
                    mTextWatcher.afterTextChanged(s);
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

    public void addTextWatcher(TextWatcher textWatcher) {
        this.mTextWatcher = textWatcher;
    }

    public interface TextWatcher {

        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);
    }
}
