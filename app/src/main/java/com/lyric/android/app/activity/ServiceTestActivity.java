package com.lyric.android.app.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.base.BaseApp;
import com.lyric.android.app.base.BaseCompatActivity;
import com.lyric.android.app.test.service.TestService;
import com.lyric.android.app.test.service.TestServiceBinder;
import com.lyric.android.app.view.TitleBar;
import com.lyric.android.library.utils.LogUtils;
import com.lyric.android.library.utils.ToastUtils;

public class ServiceTestActivity extends BaseCompatActivity {
    private boolean mServiceConnected = false;

    @Override
    public void onViewCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_service_test);

        findViewById(R.id.btn_start_service).setOnClickListener(this);
        findViewById(R.id.btn_stop_service).setOnClickListener(this);
        findViewById(R.id.btn_bind_service).setOnClickListener(this);
        findViewById(R.id.btn_unbind_service).setOnClickListener(this);
    }

    @Override
    public void onTitleCreated(TitleBar titleBar) {
    }

    @Override
    protected boolean isSwipeBackEnable() {
        return true;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.d(TAG, "onServiceConnected()--name:" + name);
            mServiceConnected = true;
            if (service instanceof TestServiceBinder) {
                TestServiceBinder serviceBinder = (TestServiceBinder) service;
                serviceBinder.start();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.d(TAG, "onServiceDisconnected()--name:" + name);
            mServiceConnected = false;
        }
    };

    @Override
    public void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.btn_start_service: {
                ToastUtils.showShort(BaseApp.getContext(), "启动服务");
                Intent service = new Intent(this, TestService.class);
                startService(service);
            }
                break;
            case R.id.btn_stop_service: {
                ToastUtils.showShort(BaseApp.getContext(), "停止服务");
                Intent service = new Intent(this, TestService.class);
                stopService(service);
            }
                break;
            case R.id.btn_bind_service: {
                ToastUtils.showShort(BaseApp.getContext(), "绑定服务");
                Intent service = new Intent(this, TestService.class);
                bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);
            }
                break;
            case R.id.btn_unbind_service: {
                if (mServiceConnected) {
                    ToastUtils.showShort(BaseApp.getContext(), "解绑服务");
                    unbindService(mServiceConnection);
                    mServiceConnected = false;
                } else {
                    ToastUtils.showShort(BaseApp.getContext(), "服务未绑定");
                }
            }
                break;
            default:
                break;
        }
    }
}
