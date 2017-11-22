package com.lyric.android.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyric.android.app.widget.dialog.LoadingDialog;
import com.lyric.utils.ViewUtils;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected final String TAG = getClass().getName();
    private View mRootView;
    private boolean mViewVisible;
    private LoadingDialog mLoadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        onPrepareCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            initExtras(args);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = getLayout(inflater);
        initView(inflater, container, savedInstanceState);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    protected void onPrepareCreate(Bundle savedInstanceState) {
    }

    protected View getLayout(LayoutInflater inflater) {
        return inflater.inflate(getLayoutId(), null);
    }

    protected abstract void initExtras(Bundle args);

    protected abstract int getLayoutId();

    protected abstract void initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    protected void onViewClick(View v) {
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastOperated()) {
            return;
        }
        onViewClick(v);
    }

    protected View getRootView() {
        return mRootView;
    }

    protected <T extends View> T findViewById(int id) {
        if (mRootView == null) {
            return null;
        }
        return (T) mRootView.findViewById(id);
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
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getActivity());
        }
        mLoadingDialog.setMessage(message);
        mLoadingDialog.show();
    }

    protected void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public boolean onBackPressed() {
        return false;
    }

    public void finishActivity() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        getActivity().finish();
    }
}
