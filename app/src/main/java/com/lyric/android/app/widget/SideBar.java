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

/**
 * @author lyricgan
 * @description
 * @time 2016/8/25 11:05
 */
public class SideBar extends View {
    private static final String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    // 触摸事件
    private OnLetterChangedListener mOnLetterChangedListener;
    private int mChoose = -1;// 选中
    private Paint mPaint = new Paint();
    private TextView tvDialog;
    private int mTextSize;

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
        mTextSize = sp2px(getContext(), 12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();
        int width = getWidth();
        // 获取每一个字母的高度
        int singleHeight = height / LETTERS.length;
        for (int i = 0; i < LETTERS.length; i++) {
            mPaint.setColor(Color.parseColor("#777777"));
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(mTextSize);
            // 选中的状态
            if (i == mChoose) {
                mPaint.setColor(Color.parseColor("#616060"));
                mPaint.setFakeBoldText(true);
            }
            // x坐标等于(中间-字符串宽度)的一半.
            float xPos = width / 2 - mPaint.measureText(LETTERS[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(LETTERS[i], xPos, yPos, mPaint);
            mPaint.reset();// 重置画笔
        }
    }

    private int sp2px(Context context, float spValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = mChoose;
        final OnLetterChangedListener listener = mOnLetterChangedListener;
        final int c = (int) (y / getHeight() * LETTERS.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(0x00000000);
                mChoose = -1;//
                invalidate();
                if (tvDialog != null) {
                    tvDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                setBackgroundColor(0x00000000);
                if (oldChoose != c) {
                    if (c >= 0 && c < LETTERS.length) {
                        if (listener != null) {
                            listener.onChanged(LETTERS[c]);
                        }
                        if (tvDialog != null) {
                            tvDialog.setText(LETTERS[c]);
                            tvDialog.setVisibility(View.VISIBLE);
                        }
                        mChoose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setTextView(TextView dialog) {
        this.tvDialog = dialog;
    }

    public void setOnLetterChangedListener(OnLetterChangedListener listener) {
        this.mOnLetterChangedListener = listener;
    }

    public interface OnLetterChangedListener {

        void onChanged(String s);
    }
}
