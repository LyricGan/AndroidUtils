package com.lyric.android.app.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyric.android.app.R;
import com.lyric.android.app.BaseApp;
import com.lyric.android.app.BaseFragment;
import com.lyric.android.app.test.service.TestService;
import com.lyric.android.app.test.service.TestServiceBinder;
import com.lyric.android.library.utils.LogUtils;
import com.lyric.android.library.utils.ToastUtils;

/**
 * 服务类测试页面
 * @author ganyu
 * @date 2017/7/25 15:05
 */
public class ServiceFragment extends BaseFragment {
    private boolean mServiceConnected = false;

    @Override
    protected void initExtras(Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_service;
    }

    @Override
    protected void initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        findViewById(R.id.btn_start_service).setOnClickListener(this);
        findViewById(R.id.btn_stop_service).setOnClickListener(this);
        findViewById(R.id.btn_bind_service).setOnClickListener(this);
        findViewById(R.id.btn_unbind_service).setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
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
                Intent service = new Intent(getActivity(), TestService.class);
                getActivity().startService(service);
            }
                break;
            case R.id.btn_stop_service: {
                ToastUtils.showShort(BaseApp.getContext(), "停止服务");
                Intent service = new Intent(getActivity(), TestService.class);
                getActivity().stopService(service);
            }
                break;
            case R.id.btn_bind_service: {
                ToastUtils.showShort(BaseApp.getContext(), "绑定服务");
                Intent service = new Intent(getActivity(), TestService.class);
                getActivity().bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);
            }
                break;
            case R.id.btn_unbind_service: {
                if (mServiceConnected) {
                    ToastUtils.showShort(BaseApp.getContext(), "解绑服务");
                    getActivity().unbindService(mServiceConnection);
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
