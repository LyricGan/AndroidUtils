package com.lyric.android.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.lyric.android.app.R;
import com.lyric.android.app.utils.MessageHandler;

/**
 * @author <a href="mailto:ganyu@medlinker.net">ganyu</a>
 * @version 3.0
 * @description 启动界面
 * @time 2015/11/2 10:57
 */
public class SplashActivity extends Activity implements View.OnClickListener {
    // 延迟加载时间
    private static final int DELAY_MILLIS = 2000;

    private Button btn_skip;

    private final MessageHandler mHandler = new MessageHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 防止HOME键重复启动
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);
        btn_skip = (Button) findViewById(R.id.btn_skip);

        initialize();
    }

    private void initialize() {
        btn_skip.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_skip: {// 跳过
                start();
            }
                break;
            default:
                break;
        }
    }

    /**
     * 跳转界面
     */
    public void start() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 发送延时消息
        Message msg = mHandler.obtainMessage(MessageHandler.MessageCode.ACTIVITY_START);
        mHandler.sendMessageDelayed(msg, DELAY_MILLIS);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 移除延时消息
//        removeInnerMessage();
    }

    private void removeInnerMessage() {
        if (mHandler.hasMessages(MessageHandler.MessageCode.ACTIVITY_START)) {
            mHandler.removeMessages(MessageHandler.MessageCode.ACTIVITY_START);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK) || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
