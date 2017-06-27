package com.lyric.android.app.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BaseFragment extends Fragment {
    private boolean mInterceptVisibleHint;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    public boolean onBackPressed() {
        return false;
    }

}
