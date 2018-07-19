package com.lyric.android.app.common;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author lyricgan
 */
public abstract class BaseViewController<T> implements IControllerCallback<T> {
    private Context mContext;
    private ViewGroup mParent;
    private View mView;
    private T mData;

    public BaseViewController(Context context, ViewGroup parent, @LayoutRes int layoutId) {
        this(context, parent, LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    public BaseViewController(Context context, ViewGroup parent, View view) {
        this.mContext = context;
        this.mParent = parent;

        init(view);
    }

    private void init(View view) {
        if (mParent != null) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams != null) {
                mParent.addView(view, layoutParams);
            } else {
                mParent.addView(view);
            }
        }
        mView = view;

        onCreateView(view);
    }

    public ViewGroup.LayoutParams getLayoutParams() {
        return null;
    }

    @Override
    public void onDestroyView() {
        if (mParent != null) {
            mParent.removeView(mView);
        }
    }

    public Context getContext() {
        return mContext;
    }

    public ViewGroup getParent() {
        return mParent;
    }

    public View getView() {
        return mView;
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        this.mData = data;

        onUpdateView(data);
    }

    public void setVisibility(int visibility) {
        if (mView != null) {
            mView.setVisibility(visibility);
        }
    }

    public int getVisibility() {
        if (mView != null) {
            return mView.getVisibility();
        }
        return View.GONE;
    }

    public <T extends View> T findViewWithId(@IdRes int id) {
        if (mView != null) {
            return (T) mView.findViewById(id);
        }
        return null;
    }

}
