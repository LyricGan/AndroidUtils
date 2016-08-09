package com.lyric.android.app.test.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author lyricgan
 * @description
 * @time 2016/8/9 14:01
 */
public abstract class BaseFragment extends Fragment {
    private static final String EXTRA_STATE_HIDDEN = "state_hidden";
    protected BaseAppActivity mHoldActivity;

    protected abstract int getLayoutId();

    protected abstract void initializeView(View view, Bundle savedInstanceState);

    protected BaseAppActivity getHoldActivity() {
        return mHoldActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mHoldActivity = (BaseAppActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isHidden = savedInstanceState.getBoolean(EXTRA_STATE_HIDDEN);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (isHidden) {
                transaction.hide(this);
            } else {
                transaction.show(this);
            }
            transaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_STATE_HIDDEN, isHidden());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initializeView(view, savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
