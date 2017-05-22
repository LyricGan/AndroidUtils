package com.lyric.android.app.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;

public class BaseFragment extends Fragment {
    private boolean mInterceptVisibleHint;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mInterceptVisibleHint && this.getView() != null) {
                getView().setVisibility(isVisibleToUser ? View.VISIBLE : View.GONE);
            }
        }
    }

    public void setInterceptVisibleHint(boolean interceptVisibleHint) {
        this.mInterceptVisibleHint = interceptVisibleHint;
    }

    public boolean isActivityDestroyed() {
        return getActivity() == null;
    }

    public void switchFragment(FragmentManager fragmentManager, SparseArray<Fragment> fragmentSparseArray, int containerViewId, Fragment currentFragment) {
        if (fragmentManager == null || fragmentSparseArray == null || containerViewId <= 0 || currentFragment == null) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragmentSparseArray.size(); i++) {
            Fragment itemFragment = fragmentSparseArray.valueAt(i);
            if (itemFragment != null && itemFragment.isAdded()) {
                fragmentTransaction.hide(itemFragment);
            }
        }
        if (currentFragment.isAdded()) {
            fragmentTransaction.show(currentFragment);
        } else {
            fragmentTransaction.add(containerViewId, currentFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

}
