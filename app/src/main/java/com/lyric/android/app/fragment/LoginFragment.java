package com.lyric.android.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lyric.android.app.R;
import com.lyric.android.app.BaseApp;
import com.lyric.android.app.BaseFragment;
import com.lyric.android.app.test.mvvm.model.User;
import com.lyric.android.app.test.mvvm.viewmodel.LoginViewModel;
import com.lyric.utils.ToastUtils;

/**
 * 登录页面
 * @author ganyu
 * @date 2017/7/25 15:19
 */
public class LoginFragment extends BaseFragment implements LoginViewModel.OnActionListener {
    private LoginViewModel mLoginViewModel;

    @Override
    protected void initExtras(Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLoginViewModel = new LoginViewModel(getRootView(), this);

        getActivity().setTitle(R.string.login);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    public void showLoading() {
        showLoadingDialog("正在登录...");
    }

    @Override
    public void loginSuccess(User user) {
        hideLoadingDialog();
        ToastUtils.showShort(BaseApp.getContext(), "登录成功，欢迎回来 " + user.name);
        finishActivity();
    }

    @Override
    public void loginFailed() {
        hideLoadingDialog();
        ToastUtils.showShort(BaseApp.getContext(), "登录失败");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoginViewModel.destroy();
    }
}
