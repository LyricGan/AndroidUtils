package com.lyric.android.app.test.mvvm.view;

import android.os.Bundle;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.base.BaseApp;
import com.lyric.android.app.base.BaseCompatActivity;
import com.lyric.android.app.test.mvvm.model.User;
import com.lyric.android.app.test.mvvm.viewmodel.LoginViewModel;
import com.lyric.android.app.view.TitleBar;
import com.lyric.android.library.utils.ToastUtils;

/**
 * @author lyric
 * @description
 * @time 2016/5/31 16:31
 */
public class LoginActivity extends BaseCompatActivity implements LoginViewModel.OnActionListener {
    private LoginViewModel mLoginViewModel;

    @Override
    public void onViewCreate(Bundle savedInstanceState) {
        View view = View.inflate(this, R.layout.activity_login, null);
        setContentView(view);
        mLoginViewModel = new LoginViewModel(view, this);
    }

    @Override
    public void onTitleCreated(TitleBar titleBar) {
        titleBar.setText("登录");
    }

    @Override
    public void showLoading() {
        showLoadingDialog("正在登录...");
    }

    @Override
    public void loginSuccess(User user) {
        hideLoadingDialog();
        ToastUtils.showShort(BaseApp.getContext(), "登录成功，欢迎回来 " + user.name);
        finish();
    }

    @Override
    public void loginFailed() {
        hideLoadingDialog();
        ToastUtils.showShort(BaseApp.getContext(), "登录失败");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginViewModel.destroy();
    }
}
