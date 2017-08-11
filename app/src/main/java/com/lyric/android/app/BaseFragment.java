package com.lyric.android.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyric.android.app.widget.dialog.LoadingDialog;
import com.lyric.utils.ViewUtils;

import java.util.List;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected final String TAG = getClass().getName();
    private View mRootView;
    private boolean mInterceptVisibleHint;
    private LoadingDialog mLoadingDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        onPrepareCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        initExtras(savedInstanceState);
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

    protected abstract void initExtras(Bundle savedInstanceState);

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
            if (mInterceptVisibleHint) {
                View view = getView();
                if (view != null) {
                    view.setVisibility(isVisibleToUser ? View.VISIBLE : View.GONE);
                }
            }
        }
    }

    @Override
    public boolean getUserVisibleHint() {
        return super.getUserVisibleHint();
    }

    public void setInterceptVisibleHint(boolean interceptVisibleHint) {
        this.mInterceptVisibleHint = interceptVisibleHint;
    }

    public boolean isActivityDestroyed() {
        return getActivity() == null;
    }

    public void switchFragment(FragmentManager fragmentManager, SparseArray<? extends BaseFragment> fragmentSparseArray, int containerViewId, BaseFragment selectFragment) {
        if (fragmentManager == null || fragmentSparseArray == null || containerViewId <= 0 || selectFragment == null) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragmentSparseArray.size(); i++) {
            BaseFragment itemFragment = fragmentSparseArray.valueAt(i);
            if (itemFragment != null && itemFragment.isAdded()) {
                fragmentTransaction.hide(itemFragment);
                itemFragment.onHide();
            }
        }
        if (selectFragment.isAdded()) {
            fragmentTransaction.show(selectFragment);
            selectFragment.onShow();
        } else {
            fragmentTransaction.add(containerViewId, selectFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void clearFragments(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isAdded()) {
                    transaction = transaction.remove(fragment);
                }
            }
            transaction.commitAllowingStateLoss();
        }
    }

    protected void onShow() {

    }

    protected void onHide() {

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

    public void finish() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        getActivity().finish();
    }

}
