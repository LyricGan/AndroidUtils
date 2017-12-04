package com.lyric.android.app.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseFragment;
import com.lyric.android.app.widget.CircleProgressBar;

/**
 * 加载进度页面
 * @author lyricgan
 * @date 2017/7/25 14:24
 */
public class ProgressBarFragment extends BaseFragment {
    private CircleProgressBar mLineProgressBar;
    private CircleProgressBar mSolidProgressBar;
    private CircleProgressBar mCustomProgressBar1;
    private CircleProgressBar mCustomProgressBar2;
    private CircleProgressBar mCustomProgressBar3;
    private CircleProgressBar mCustomProgressBar4;
    private CircleProgressBar mCustomProgressBar5;
    private CircleProgressBar mCustomProgressBar6;

    public static ProgressBarFragment newInstance() {
        Bundle args = new Bundle();
        ProgressBarFragment fragment = new ProgressBarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_progress_bar;
    }

    @Override
    public void onViewInitialize(View view, Bundle savedInstanceState) {
        mLineProgressBar = findViewWithId(R.id.line_progress);
        mSolidProgressBar = findViewWithId(R.id.solid_progress);
        mCustomProgressBar1 = findViewWithId(R.id.custom_progress1);
        mCustomProgressBar2 = findViewWithId(R.id.custom_progress2);
        mCustomProgressBar3 = findViewWithId(R.id.custom_progress3);
        mCustomProgressBar4 = findViewWithId(R.id.custom_progress4);
        mCustomProgressBar5 = findViewWithId(R.id.custom_progress5);
        mCustomProgressBar6 = findViewWithId(R.id.custom_progress6);
    }

    @Override
    public void onResume() {
        super.onResume();
        simulateProgress();
    }

    private void simulateProgress() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 98);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                mLineProgressBar.setProgress(progress);
                mSolidProgressBar.setProgress(progress);
                mCustomProgressBar1.setProgress(progress);
                mCustomProgressBar2.setProgress(progress);
                mCustomProgressBar3.setProgress(progress);
                mCustomProgressBar4.setProgress(progress);
                mCustomProgressBar5.setProgress(progress);
                mCustomProgressBar6.setProgress(progress);
            }
        });
        animator.setDuration(2000);
        animator.start();
    }
}
