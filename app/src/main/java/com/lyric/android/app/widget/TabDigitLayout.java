package com.lyric.android.app.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyricgan
 * @time 2017/6/12 14:04
 */
public class TabDigitLayout extends LinearLayout {
    private List<Integer> mTargetNumbers = new ArrayList<>();
    private List<TabDigit> mTextScrollViews = new ArrayList<>();
    private int mBackgroundId = -1;
    private int mTextSize = 120;

    public TabDigitLayout(Context context) {
        this(context, null);
    }

    public TabDigitLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabDigitLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }

    public void setTextBackground(int backgroundId) {
        this.mBackgroundId = backgroundId;
    }

    public void setNumber(int number, long delayMillis) {
        resetView();
        while (number > 0) {
            mTargetNumbers.add(number % 10);
            number /= 10;
        }
        for (int i = mTargetNumbers.size() - 1; i >= 0; i--) {
            TabDigit tabDigit = new TabDigit(getContext());
            tabDigit.setTextSize(mTextSize);
            tabDigit.setFrom(0);
            tabDigit.setTo(mTargetNumbers.get(i));
            tabDigit.elapsedTime(delayMillis);
            tabDigit.setCornerSize(5);
            mTextScrollViews.add(tabDigit);

            addView(tabDigit);
        }
        start(delayMillis);
    }

    private void resetView() {
        mTargetNumbers.clear();
        mTextScrollViews.clear();
        removeAllViews();
    }

    private void start(final long delayMillis) {
        for (TabDigit tabDigit : mTextScrollViews) {
            final TabDigit tabDigitView = tabDigit;
            ViewCompat.postOnAnimationDelayed(tabDigitView, new Runnable() {
                @Override
                public void run() {
                    int from = tabDigitView.getFrom();
                    int to = tabDigitView.getTo();
                    if (from < to) {
                        tabDigitView.start();
                        from++;
                        tabDigitView.setFrom(from);
                        ViewCompat.postOnAnimationDelayed(tabDigitView, this, delayMillis);
                    }
                }
            }, delayMillis);
        }
    }

    public void destroy() {
        resetView();
    }

}
