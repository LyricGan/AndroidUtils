package com.lyric.android.app.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.R;
import com.lyric.common.BaseFragment;
import com.lyric.android.app.test.service.TestService;
import com.lyric.android.app.test.service.TestServiceBinder;
import com.lyric.android.app.utils.LogUtils;
import com.lyric.android.app.utils.ToastUtils;

/**
 * 服务测试页面
 * @author lyricgan
 * @date 2017/7/25 15:05
 */
public class ServiceFragment extends BaseFragment {
    private boolean mServiceConnected = false;

    public static ServiceFragment newInstance() {
        Bundle args = new Bundle();
        ServiceFragment fragment = new ServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_service;
    }

    @Override
    public void onContentViewInitialize(View view, Bundle savedInstanceState) {
        findViewByIdRes(R.id.btn_start_service).setOnClickListener(this);
        findViewByIdRes(R.id.btn_stop_service).setOnClickListener(this);
        findViewByIdRes(R.id.btn_bind_service).setOnClickListener(this);
        findViewByIdRes(R.id.btn_unbind_service).setOnClickListener(this);
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
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_start_service: {
                ToastUtils.showShort(AndroidApplication.getContext(), "启动服务");
                Intent service = new Intent(getActivity(), TestService.class);
                getActivity().startService(service);
            }
                break;
            case R.id.btn_stop_service: {
                ToastUtils.showShort(AndroidApplication.getContext(), "停止服务");
                Intent service = new Intent(getActivity(), TestService.class);
                getActivity().stopService(service);
            }
                break;
            case R.id.btn_bind_service: {
                ToastUtils.showShort(AndroidApplication.getContext(), "绑定服务");
                Intent service = new Intent(getActivity(), TestService.class);
                getActivity().bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);
            }
                break;
            case R.id.btn_unbind_service: {
                if (mServiceConnected) {
                    ToastUtils.showShort(AndroidApplication.getContext(), "解绑服务");
                    getActivity().unbindService(mServiceConnection);
                    mServiceConnected = false;
                } else {
                    ToastUtils.showShort(AndroidApplication.getContext(), "服务未绑定");
                }
            }
                break;
            default:
                break;
        }
    }
}
