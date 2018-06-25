package com.lyric.android.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseActivity;
import com.lyric.android.app.utils.ActivityUtils;

/**
 * splash activity
 * @author lyricgan
 */
public class SplashActivity extends BaseActivity {
    private static final int MSG_CODE_START = 0x1001;
    private static final long DELAY_MILLIS = 800L;
    private long mStartTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onContentViewInitialize(View view, Bundle savedInstanceState) {
        // avoid repeat start
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
            case MSG_CODE_START:
                jumpPage();
                break;
            default:
                break;
        }
    }

    private void removeDelayedMessage() {
        if (getHandler().hasMessages(MSG_CODE_START)) {
            getHandler().removeMessages(MSG_CODE_START);
        }
    }

    private void sendDelayedMessage(long startTime) {
        long diff = System.currentTimeMillis() - startTime;
        if (diff >= DELAY_MILLIS) {
            getHandler().sendEmptyMessageDelayed(MSG_CODE_START, 0);
        } else {
            getHandler().sendEmptyMessageDelayed(MSG_CODE_START, DELAY_MILLIS - diff);
        }
    }

    private void jumpPage() {
        ActivityUtils.startActivity(this, MainActivity.class);
        finish();
    }
}
