package com.lyric.android.app.common;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * controller for view
 * @author lyricgan
 */
public abstract class ViewController<E> implements IControllerCallback<E> {
    private Context mContext;
    private ViewGroup mParent;
    private View mView;
    private E mData;

    public ViewController(Context context, @LayoutRes int layoutId, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(layoutId, parent, false), parent);
    }

    public ViewController(Context context, View view, ViewGroup parent) {
        this.mContext = context;
        this.mParent = parent;

        init(view);

        onCreateView(view);
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

    public E getData() {
        return mData;
    }

    public void setData(E data) {
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

    public <T extends View> T findViewById(@IdRes int id) {
        if (mView != null) {
            return (T) mView.findViewById(id);
        }
        return null;
    }
}
