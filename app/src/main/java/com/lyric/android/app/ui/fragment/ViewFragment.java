package com.lyric.android.app.ui.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseFragment;
import com.lyric.android.app.utils.DisplayUtils;
import com.lyric.android.app.utils.StringUtils;
import com.lyric.android.app.widget.CircleProgressBar;
import com.lyric.android.app.widget.ClashBar;
import com.lyric.android.app.widget.HorizontalRatioBar;
import com.lyric.android.app.widget.PieView;
import com.lyric.android.app.widget.RingProgressBar;
import com.lyric.android.app.widget.TabDigitLayout;

import java.util.ArrayList;
import java.util.Random;

/**
 * view fragment
 *
 * @author lyricgan
 */
public class ViewFragment extends BaseFragment {
    private final int[] mRedGradientColors = {0xffff0000, 0xffff6f43, 0xffff0000};
    private final int[] mBlueGradientColors = {0xff1fbbe9, 0xff59d7fc, 0xff1fbbe9};
    private final int[] mGreenGradientColors = {0xffb3c526, 0xff7fb72f, 0xffb3c526};

    private ClashBar mClashBar;

    public static ViewFragment newInstance() {
        Bundle args = new Bundle();
        ViewFragment fragment = new ViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_view;
    }

    @Override
    public void onCreateContentView(View view, Bundle savedInstanceState) {
        PieView pieView = findViewById(R.id.pie_view);

        ArrayList<PieView.PieData> dataList = new ArrayList<>();
        PieView.PieData data;
        for (int i = 0; i < 5; i++) {
            data = new PieView.PieData("i" + i, 100 + (i * 50));

            dataList.add(data);
        }
        pieView.setData(dataList);
        pieView.setStartAngle(0);

        final TabDigitLayout tabDigitLayout = findViewById(R.id.tab_digit_layout);
        tabDigitLayout.setTextBackground(DisplayUtils.getColor(AndroidApplication.getContext(), R.color.color_blue_default));
        tabDigitLayout.setNumber(567890, 1L);
        tabDigitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabDigitLayout.setNumber(567890, 300L);
            }
        });

        mClashBar = findViewById(R.id.clash_bar);
        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClashBar();

                showRingProgressBar();
            }
        });
        HorizontalRatioBar horizontalRatioBar = findViewById(R.id.horizontal_radio_bar);
        showRadioBar(horizontalRatioBar);
    }

    @Override
    public void onCreateData(Bundle savedInstanceState) {
        showCircleProgress();

        showClashBar();

        showRingProgressBar();
    }

    private void showCircleProgress() {
        final CircleProgressBar lineProgressBar = findViewById(R.id.line_progress);
        final CircleProgressBar solidProgressBar = findViewById(R.id.solid_progress);
        final CircleProgressBar customProgressBar1 = findViewById(R.id.custom_progress1);
        final CircleProgressBar customProgressBar2 = findViewById(R.id.custom_progress2);
        final CircleProgressBar customProgressBar3 = findViewById(R.id.custom_progress3);
        final CircleProgressBar customProgressBar4 = findViewById(R.id.custom_progress4);
        final CircleProgressBar customProgressBar5 = findViewById(R.id.custom_progress5);
        final CircleProgressBar customProgressBar6 = findViewById(R.id.custom_progress6);

        ValueAnimator animator = ValueAnimator.ofInt(0, 98);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                lineProgressBar.setProgress(progress);
                solidProgressBar.setProgress(progress);
                customProgressBar1.setProgress(progress);
                customProgressBar2.setProgress(progress);
                customProgressBar3.setProgress(progress);
                customProgressBar4.setProgress(progress);
                customProgressBar5.setProgress(progress);
                customProgressBar6.setProgress(progress);
            }
        });
        animator.setDuration(2000);
        animator.start();
    }

    private void showClashBar() {
        final float leftData = 37.5f + new Random().nextInt(1000);
        final float rightData = 12.5f + new Random().nextInt(1000);
        mClashBar.setOnClashBarUpdatedListener(new ClashBar.OnClashBarUpdatedListener() {
            @Override
            public void onChanged(float leftData, float rightData, float leftProgressData, float rightProgressData, boolean isFinished) {
                Log.d(TAG, "leftData:" + leftData + ",rightData:" + rightData + ",leftProgressData:" + leftProgressData
                        + ",rightProgressData:" + rightProgressData + ",isFinished:" + isFinished);
            }
        });
        mClashBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                mClashBar.setData(leftData, rightData, true);
            }
        }, 300L);
    }

    private void showRingProgressBar() {
        RingProgressBar ringProgressBar1 = findViewById(R.id.ring_progress_bar_1);
        RingProgressBar ringProgressBar2 = findViewById(R.id.ring_progress_bar_2);
        RingProgressBar ringProgressBar3 = findViewById(R.id.ring_progress_bar_3);

        ringProgressBar1.setAlwaysShowAnimation(true);
        ringProgressBar1.setSweepGradientColors(mRedGradientColors);
        ringProgressBar1.setProgress(36f, 100);

        ringProgressBar2.setAlwaysShowAnimation(true);
        ringProgressBar2.setSweepGradientColors(mBlueGradientColors);
        ringProgressBar2.setProgress(54f, 100);

        ringProgressBar3.setAlwaysShowAnimation(false);
        ringProgressBar3.setSweepGradientColors(mGreenGradientColors);
        ringProgressBar3.setProgress(64f, 100);
    }

    private void showRadioBar(HorizontalRatioBar horizontalRatioBar) {
        float homeRadio = 0.50f;
        float flatRadio = 0.20f;
        float awayRadio = 0.30f;
        float[] ratios = {homeRadio, flatRadio, awayRadio};
        int[] ratioColors = {0xffeb221f, 0xffb3c526, 0xff1fbbe9};
        String homeRadioString = StringUtils.formatDecimals(homeRadio * 100, 0, false) + "%";
        String flatRadioString = StringUtils.formatDecimals(flatRadio * 100, 0, false) + "%";
        String awayRadioString = StringUtils.formatDecimals(awayRadio * 100, 0, false) + "%";
        String[] radioTexts = {homeRadioString, flatRadioString, awayRadioString};
        horizontalRatioBar.setRatios(ratios, ratioColors, radioTexts);
    }

}
