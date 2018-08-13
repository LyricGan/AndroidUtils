package com.lyric.android.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View {
    private static final String[] DEFAULT_LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private String[] mLetters = DEFAULT_LETTERS;
    private int mChooseIndex = -1;
    private Paint mPaint;
    private int mColor;
    private int mSelectColor;
    private int mTextSize;

    private TextView tvLetter;
    private OnLetterChangedListener mOnLetterChangedListener;

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mColor = Color.parseColor("#777777");
        mSelectColor = Color.parseColor("#616060");
        mTextSize = sp2px(getContext(), 12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / mLetters.length;
        for (int i = 0; i < mLetters.length; i++) {
            mPaint.setColor(mColor);
            mPaint.setTextSize(mTextSize);

            if (i == mChooseIndex) {
                mPaint.setColor(mSelectColor);
                mPaint.setFakeBoldText(true);
            }
            // x坐标等于(中间-字符串宽度)的一半.
            float xPos = width / 2 - mPaint.measureText(mLetters[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(mLetters[i], xPos, yPos, mPaint);
            mPaint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int chooseIndex = mChooseIndex;
        // 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数
        int count = (int) (event.getY() / getHeight() * mLetters.length);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(0x00000000);
                mChooseIndex = -1;//
                invalidate();
                if (tvLetter != null) {
                    tvLetter.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                setBackgroundColor(0x00000000);
                if (chooseIndex != count) {
                    if (count >= 0 && count < mLetters.length) {
                        if (mOnLetterChangedListener != null) {
                            mOnLetterChangedListener.onChanged(mLetters[count]);
                        }
                        if (tvLetter != null) {
                            tvLetter.setText(mLetters[count]);
                            tvLetter.setVisibility(View.VISIBLE);
                        }
                        mChooseIndex = count;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public int sp2px(Context context, float value) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    public void setLetters(String[] letters) {
        this.mLetters = letters;
    }

    public void setTextView(TextView textView) {
        this.tvLetter = textView;
    }

    public void setOnLetterChangedListener(OnLetterChangedListener listener) {
        this.mOnLetterChangedListener = listener;
    }

    public interface OnLetterChangedListener {

        void onChanged(String s);
    }
}
