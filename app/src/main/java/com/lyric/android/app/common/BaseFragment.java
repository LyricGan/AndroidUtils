package com.lyric.android.app.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyric.android.app.R;
import com.lyric.android.app.utils.ViewUtils;

/**
 * Fragment基类
 * @author lyricgan
 * @time 2017/11/26 13:59
 */
public abstract class BaseFragment extends Fragment implements BaseListener {
    protected final String TAG = getClass().getName();
    private BaseActivity mActivity;
    private View mRootView;
    private boolean mViewVisible;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loggingMessage("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        onPrepareCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        loggingMessage("onCreate");
        Bundle bundle = getArguments();
        if (bundle != null) {
            onExtrasInitialize(bundle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loggingMessage("onCreateView");
        View rootView = inflater.inflate(getLayoutId(), null);
        mRootView = rootView;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loggingMessage("onViewCreated");
        View titleView = view.findViewById(R.id.title_bar);
        if (titleView != null) {
            BaseTitleBar titleBar = new BaseTitleBar(titleView);
            onTitleBarInitialize(titleBar, savedInstanceState);
        }
        onContentViewInitialize(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loggingMessage("onActivityCreated");
        onDataInitialize(savedInstanceState);
    }

    @Override
    public void onPrepareCreate(Bundle savedInstanceState) {
    }

    @Override
    public void onExtrasInitialize(Bundle bundle) {
    }

    @Override
    public void onTitleBarInitialize(BaseTitleBar titleBar, Bundle savedInstanceState) {
    }

    @Override
    public void onDataInitialize(Bundle savedInstanceState) {
    }

    @Override
    public void onViewClick(View v) {
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastOperated()) {
            return;
        }
        onViewClick(v);
    }

    @Override
    public <T extends View> T findViewWithId(int id) {
        View rootView = getRootView();
        if (rootView == null) {
            return null;
        }
        return (T) rootView.findViewById(id);
    }

    @Override
    public void onStart() {
        super.onStart();
        loggingMessage("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        loggingMessage("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        loggingMessage("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        loggingMessage("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loggingMessage("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loggingMessage("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        loggingMessage("onDetach");
    }

    protected View getRootView() {
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mViewVisible) {
                View view = getView();
                if (view != null) {
                    view.setVisibility(isVisibleToUser ? View.VISIBLE : View.GONE);
                }
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public BaseActivity getBaseActivity() {
        if (mActivity == null) {
            mActivity = (BaseActivity) getActivity();
        }
        return mActivity;
    }

    public void setViewVisible(boolean viewVisible) {
        this.mViewVisible = viewVisible;
    }

    public boolean isViewVisible() {
        View view = getView();
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public boolean isActivityDestroyed() {
        return getActivity() == null;
    }

    protected void showLoadingDialog() {
        showLoadingDialog("");
    }

    protected void showLoadingDialog(CharSequence message) {
        showLoadingDialog(message, true, false);
    }

    protected void showLoadingDialog(CharSequence message, boolean cancelable, boolean canceledOnTouchOutside) {
        BaseActivity activity = getBaseActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (!isStatusValid()) {
            return;
        }
        activity.showLoadingDialog(message, cancelable, canceledOnTouchOutside);
    }

    protected void hideLoadingDialog() {
        BaseActivity activity = getBaseActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (!isStatusValid()) {
            return;
        }
        activity.hideLoadingDialog();
    }

    public boolean isStatusValid() {
        return (isAdded() && !isRemoving());
    }

    public boolean onBackPressed() {
        return false;
    }

    public void finishActivity() {
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.finish();
    }

    private void loggingMessage(String message) {
        Log.d(TAG, message);
    }

    @Override
    public Handler getHandler() {
        BaseActivity activity = getBaseActivity();
        if (activity != null) {
            return activity.getHandler();
        }
        return null;
    }

    @Override
    public void handleMessage(Message msg) {
    }
}
