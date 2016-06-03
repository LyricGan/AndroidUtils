package com.lyric.android.app.mvvm.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.lyric.android.app.R;
import com.lyric.android.app.base.BaseApplication;
import com.lyric.android.app.mvvm.model.Repository;
import com.lyric.android.app.mvvm.model.User;
import com.lyric.android.app.mvvm.model.UserApi;
import com.lyric.android.library.utils.LogUtils;

import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
        void startLogin();

        void loginSuccess(User user);

        void loginFailed();
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
        mActionListener.startLogin();
        LogUtils.d(TAG, "userName:" + userName + ",password:" + password);
        UserApi userApi = UserApi.Factory.getInstance();
        userApi.getRepositoryList(userName).enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Response<List<Repository>> response, Retrofit retrofit) {
                List<Repository> repositoryList = response.body();
                if (repositoryList != null && !repositoryList.isEmpty()) {
                    Repository repository = repositoryList.get(0);
                    mActionListener.loginSuccess(repository.owner);
                } else {
                    mActionListener.loginFailed();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mActionListener.loginFailed();
            }
        });
    }

    @Override
    public void destroy() {

    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) BaseApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit_user_name.getWindowToken(), 0);
    }
}
