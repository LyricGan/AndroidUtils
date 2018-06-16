package com.lyric.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.test.Test;
import com.lyric.android.app.common.BaseActivity;
import com.lyric.android.app.utils.ActivityUtils;

/**
 * splash activity
 * @author lyricgan
 */
public class SplashActivity extends BaseActivity {
    private static final int WHAT_START = 0x1001;
    // delay millis
    private static final long DELAY_MILLIS = 800L;
    private long mStartTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onContentViewInitialize(View view, Bundle savedInstanceState) {
        // for home repeat start
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        findViewById(R.id.btn_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDelayedMessage();
                jumpPage();
            }
        });
        mStartTime = System.currentTimeMillis();

        Test.dirs(AndroidApplication.getContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendDelayedMessage(mStartTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeDelayedMessage();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case WHAT_START:
                jumpPage();
                break;
            default:
                break;
        }
    }

    private void removeDelayedMessage() {
        if (getHandler().hasMessages(WHAT_START)) {
            getHandler().removeMessages(WHAT_START);
        }
    }

    private void sendDelayedMessage(long startTime) {
        long diff = System.currentTimeMillis() - startTime;
        if (diff >= DELAY_MILLIS) {
            getHandler().sendEmptyMessageDelayed(WHAT_START, 0);
        } else {
            getHandler().sendEmptyMessageDelayed(WHAT_START, DELAY_MILLIS - diff);
        }
    }

    private void jumpPage() {
        ActivityUtils.startActivity(this, MainActivity.class);
        finish();
    }
}
