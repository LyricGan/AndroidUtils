package com.lyricgan.demo.util.widget.praiseview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lyricgan.demo.util.R;
import com.lyricgan.demo.util.widget.praiseview.animation.AnimateHelper;

public class PraiseView extends FrameLayout implements Checkable {
    protected OnPraiseCheckedListener praiseCheckedListener;
    protected CheckedImageView mImageView;
    protected TextView mTextView;
    protected int mPadding;

    public PraiseView(Context context) {
        super(context);
        initialize();
    }

    public PraiseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    protected void initialize() {
        setClickable(true);
        mImageView = new CheckedImageView(getContext());
        mImageView.setImageResource(R.drawable.blog_praise_selector);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        addView(mImageView, params);

        mTextView = new TextView(getContext());
        mTextView.setTextSize(12);
        mTextView.setText("+1");
        mTextView.setTextColor(Color.parseColor("#ff0000"));
        mTextView.setGravity(Gravity.CENTER);
        addView(mTextView, params);
        mTextView.setVisibility(View.GONE);
    }

    @Override
    public boolean performClick() {
        checkChange();
        return super.performClick();
    }

    @Override
    public void toggle() {
        checkChange();
    }

    public void setChecked(boolean isCheacked) {
        mImageView.setChecked(isCheacked);
    }

    public void checkChange() {
        if (mImageView.isChecked) {
            mImageView.setChecked(false);
        } else {
            mImageView.setChecked(true);
            mTextView.setVisibility(View.VISIBLE);
            AnimateHelper.withPulse().duration(1000).playOn(mImageView);
            AnimateHelper.withSlideOutUp().duration(1000).playOn(mTextView);
        }
        if (praiseCheckedListener != null) {
            praiseCheckedListener.onPraiseChecked(mImageView.isChecked);
        }
    }

    public boolean isChecked() {
        return mImageView.isChecked;
    }

    public void setOnPraiseCheckedListener(OnPraiseCheckedListener praiseCheckedListener) {
        this.praiseCheckedListener = praiseCheckedListener;
    }

    public interface OnPraiseCheckedListener {

        void onPraiseChecked(boolean isChecked);
    }
}

