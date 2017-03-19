package com.lyric.android.app.widget.swipe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.library.utils.ViewUtils;

/**
 * 滑动关闭帮助类
 * 
 * @author lyricgan
 *
 */
public class SwipeBackActivityHelper {
	/** 震动持续时间 */
	public static final int VIBRATE_DURATION = 20;
    private Activity mActivity;
    private SwipeBackLayout mSwipeBackLayout;

    public SwipeBackActivityHelper(Activity activity) {
        mActivity = activity;
    }

	public void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = (SwipeBackLayout) View.inflate(mActivity, R.layout.view_swipeback_layout, null);
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {}

            @Override
            public void onEdgeTouch(int edgeFlag) {
                ViewUtils.convertActivityToTranslucent(mActivity);
            }

            @Override
            public void onScrollOverThreshold() {
            }
        });
    }
    
    /**
     * 滑动带震动效果
     * @param duration 震动时长
     */
    protected void startVibrate(long duration) {
		Vibrator vibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 0, duration };
		vibrator.vibrate(pattern, -1);
	}

    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    public View findViewById(int id) {
        if (mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return null;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
    
}
