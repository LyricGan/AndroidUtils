package com.lyric.android.app.test.mvvm.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.lyric.android.app.R;
import com.lyric.android.app.BaseApp;
import com.lyric.android.app.test.mvvm.model.ApiFactory;
import com.lyric.android.app.test.mvvm.model.Repository;
import com.lyric.android.app.test.mvvm.model.User;
import com.lyric.utils.LogUtils;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author lyric
 * @time 2016/5/31 16:31
 */
public class LoginViewModel implements ViewModel, View.OnClickListener {
    private static final String TAG = LoginViewModel.class.getSimpleName();
    private EditText editUserName;
    private EditText editPassword;

    private OnActionListener mActionListener;

    public interface OnActionListener {

        void showLoading();

        void loginSuccess(User user);

        void loginFailed();
    }

    public LoginViewModel(View view, OnActionListener listener) {
        this.mActionListener = listener;
        editUserName = (EditText) view.findViewById(R.id.edit_user_name);
        editPassword = (EditText) view.findViewById(R.id.edit_password);
        Button btn_login = (Button) view.findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                String userName = editUserName.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    editUserName.requestFocus();
                    break;
                }
                if (TextUtils.isEmpty(password)) {
                    editPassword.requestFocus();
                    break;
                }
                hideSoftKeyboard(editUserName);
                login(userName, password);
            }
                break;
            default:
                break;
        }
    }

    private void login(String userName, String password) {
        mActionListener.showLoading();
        ApiFactory.getUserApi().getRepositoryList(userName)
                .flatMap(new Func1<List<Repository>, Observable<User>>() {
                    @Override
                    public Observable<User> call(List<Repository> repositories) {
                        if (repositories != null && !repositories.isEmpty()) {
                            Repository repository = repositories.get(0);
                            if (repository.owner != null) {
                                return ApiFactory.getUserApi().getUserDetails(repository.owner.url);
                            }
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e(TAG, "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActionListener.loginFailed();
                    }

                    @Override
                    public void onNext(User user) {
                        if (user != null) {
                            mActionListener.loginSuccess(user);
                        }  else {
                            mActionListener.loginFailed();
                        }
                    }
                });
    }

//    private void onProcessRepository(String userName) {
//        Api.getUserApi().getRepositoryList(userName).enqueue(new Callback<List<Repository>>() {
//            @Override
//            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
//                List<Repository> repositoryList = response.body();
//                if (repositoryList != null && !repositoryList.isEmpty()) {
//                    Repository repository = repositoryList.get(0);
//                    if (repository.owner != null) {
//                        onProcessUser(repository.owner);
//                    }
//                } else {
//                    mActionListener.loginFailed();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Repository>> call, Throwable t) {
//                mActionListener.loginFailed();
//            }
//        });
//    }
//
//    private void onProcessUser(User user) {
//        Api.getUserApi().getUserDetails(user.url).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                User data = response.body();
//                if (data != null) {
//                    mActionListener.loginSuccess(data);
//                }  else {
//                    mActionListener.loginFailed();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                mActionListener.loginFailed();
//            }
//        });
//    }

    private void hideSoftKeyboard(EditText editText) {
        if (editText == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) BaseApp.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    @Override
    public void destroy() {
    }
}
