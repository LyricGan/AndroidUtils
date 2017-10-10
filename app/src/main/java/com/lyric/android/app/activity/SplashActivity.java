package com.lyric.android.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.lyric.android.app.BaseActivity;
import com.lyric.android.app.R;
import com.lyric.android.app.test.handler.WeakHandler;

/**
 * @author lyricgan
 * @description 启动页面
 * @time 2015/11/2 10:57
 */
public class SplashActivity extends BaseActivity {
    private static final int WHAT_START = 0x1001;
    // 延迟加载时间
    private static final int DELAY_MILLIS = 3000;
    private final WeakHandler mHandler = new WeakHandler<>(this);
    // 启动时间
    private long mStartTime;

    @Override
    public void onViewCreate(Bundle savedInstanceState) {
        // 防止HOME键重复启动
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        Button btn_skip = (Button) findViewById(R.id.btn_skip);

        btn_skip.setOnClickListener(this);
        mHandler.setCallback(new WeakHandler.OnMessageCallback() {
            @Override
            public void callback(Message msg) {
                if (WHAT_START == msg.what) {
                    start();
                }
            }
        });
        mStartTime = System.currentTimeMillis();
    }

    @Override
    public void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.btn_skip: {// 跳过
                start();
            }
                break;
            default:
                break;
        }
    }

    private void start() {
        finish();
    }

    // 发送延时消息
    private void sendDelayedMessage(long delayMillis) {
        mHandler.sendEmptyMessageDelayed(WHAT_START, delayMillis);
    }

    private void removeDelayedMessage() {
        if (mHandler.hasMessages(WHAT_START)) {
            mHandler.removeMessages(WHAT_START);
        }
    }

    private void sendDelayedMessage() {
        long diff = System.currentTimeMillis() - mStartTime;
        if (diff >= DELAY_MILLIS) {
            sendDelayedMessage(0);
        } else {
            sendDelayedMessage(DELAY_MILLIS - diff);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendDelayedMessage();
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
    protected void onDestroy() {
        removeDelayedMessage();
        mHandler.removeCallback();
        super.onDestroy();
    }
}
