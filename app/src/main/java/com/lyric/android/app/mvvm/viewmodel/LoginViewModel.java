package com.lyric.android.app.mvvm.viewmodel;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.lyric.android.app.BaseApplication;
import com.lyric.android.app.R;
import com.lyric.android.library.utils.LogUtils;

/**
 * @author lyric
 * @description
 * @time 2016/5/31 16:31
 */
public class LoginViewModel implements ViewModel, View.OnClickListener {
    private static final String TAG = LoginViewModel.class.getSimpleName();
    private EditText edit_user_name;
    private EditText edit_password;

    private OnActionListener mActionListener;

    public interface OnActionListener {
        void showLoading();

        void loginSuccess();

        void dismissLoading();
    }

    public LoginViewModel(View view, OnActionListener listener) {
        this.mActionListener = listener;
        edit_user_name = (EditText) view.findViewById(R.id.edit_user_name);
        edit_password = (EditText) view.findViewById(R.id.edit_password);
        Button btn_login = (Button) view.findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                String userName = edit_user_name.getText().toString().trim();
                String password = edit_password.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    break;
                }
                if (TextUtils.isEmpty(password)) {
                    break;
                }
                hideSoftKeyboard();
                login(userName, password);
            }
                break;
        }
    }

    private void login(String userName, String password) {
        mActionListener.showLoading();
        LogUtils.d(TAG, "userName:" + userName + ",password:" + password);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActionListener.loginSuccess();
                mActionListener.dismissLoading();
            }
        }, 2000);
    }

    @Override
    public void destroy() {

    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) BaseApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit_user_name.getWindowToken(), 0);
    }
}
