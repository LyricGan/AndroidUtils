package com.lyric.android.app.utils;

import android.view.View;
import android.view.ViewConfiguration;

/**
 * 视图双击回调事件
 * @author lyricgan
 */
public abstract class OnDoubleClickListener implements View.OnClickListener {
    // ViewConfiguration.getDoubleTapMinTime()
    private static final int DOUBLE_TAP_MIN_TIME = 40;
    /** 记录上一次点击时间 */
    private long mLastClickTime;
    /** 记录上一次双击时间 */
    private long mLastDoubleTime;

    @Override
    public void onClick(View v) {
        final long time = System.currentTimeMillis();
        final long delta = time - mLastClickTime;
        if (delta <= ViewConfiguration.getDoubleTapTimeout() && delta >= DOUBLE_TAP_MIN_TIME && (time - mLastDoubleTime) > 2000) {
            onDoubleClick(v);
            mLastDoubleTime = time;
        } else {
            mLastClickTime = time;
        }
    }

    /**
     * 双击视图回调
     * @param v View
     */
    public abstract void onDoubleClick(View v);
}
