package com.lyric.android.app.common;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 适配器ViewHolder
 * @author lyricgan
 * @time 16/3/10
 */
public class BaseViewHolder {
    private Context mContext;
    private SparseArray<View> mViewArray;
    private View mConvertView;
    private Object mAssociatedObject;

    private BaseViewHolder(ViewGroup parent, int layoutId) {
        this.mContext = parent.getContext();
        this.mViewArray = new SparseArray<View>();
        this.mConvertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
    }

    public static BaseViewHolder get(View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            return new BaseViewHolder(parent, layoutId);
        } else {
            return (BaseViewHolder) convertView.getTag();
        }
    }

    public View getView() {
        return mConvertView;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViewArray.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViewArray.put(viewId, view);
        }
        return (T) view;
    }

    public void clear() {
        mViewArray.clear();
    }

    public Context getContext() {
        return mContext;
    }

    public Object getAssociatedObject() {
        return mAssociatedObject;
    }

    public void setAssociatedObject(Object associatedObject) {
        this.mAssociatedObject = associatedObject;
    }

    public boolean isItemChanged(Object item) {
        boolean itemChanged = (getAssociatedObject() == null || !getAssociatedObject().equals(item));
        setAssociatedObject(item);
        return itemChanged;
    }

    public BaseViewHolder setVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

    public BaseViewHolder toggleVisibility(int viewId) {
        View view = getView(viewId);
        if (View.VISIBLE == view.getVisibility()) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public BaseViewHolder setEnabled(int viewId, boolean enabled) {
        getView(viewId).setEnabled(enabled);
        return this;
    }

    public BaseViewHolder setText(int viewId, int textId) {
        return setText(viewId, mContext.getString(textId));
    }

    public BaseViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public BaseViewHolder setTextAppearance(int viewId, int redId) {
        TextView textView = getView(viewId);
        if (Build.VERSION.SDK_INT >= 23) {
            textView.setTextAppearance(redId);
        } else {
            textView.setTextAppearance(mContext, redId);
        }
        return this;
    }

    public BaseViewHolder setTextColor(int viewId, int textColor) {
        TextView textView = getView(viewId);
        textView.setTextColor(textColor);
        return this;
    }

    public BaseViewHolder setTextColor(int viewId, ColorStateList colorList) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorList);
        return this;
    }

    public BaseViewHolder setTextColorResource(int viewId, int textColorResId) {
        return setTextColor(viewId, textColorResId);
    }

    public BaseViewHolder setTextColorStateListRes(int viewId, int textColorStateListResId) {
        return setTextColor(viewId, textColorStateListResId);
    }

    public BaseViewHolder setTextSizeDp(int viewId, float size) {
        ((TextView) getView(viewId)).setTextSize(size);
        return this;
    }

    public BaseViewHolder setTextSize(int viewId, float size) {
        ((TextView) getView(viewId)).setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        return this;
    }

    public BaseViewHolder setTextDrawable(int viewId, int left, int top, int right, int bottom) {
        ((TextView) getView(viewId)).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }

    public BaseViewHolder setTextDrawable(int viewId, Drawable left, Drawable top, Drawable right, Drawable bottom) {
        ((TextView) getView(viewId)).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }

    public BaseViewHolder setTextDrawablePadding(int viewId, int padding) {
        ((TextView) getView(viewId)).setCompoundDrawablePadding(padding);
        return this;
    }

    public BaseViewHolder setBackgroundColor(int viewId, int color) {
        getView(viewId).setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setBackgroundResource(int viewId, int backgroundResource) {
        getView(viewId).setBackgroundResource(backgroundResource);
        return this;
    }

    public BaseViewHolder setBackgroundDrawable(int viewId, Drawable background) {
        if (Build.VERSION.SDK_INT >= 16) {
            getView(viewId).setBackground(background);
        } else {
            getView(viewId).setBackgroundDrawable(background);
        }
        return this;
    }

    public BaseViewHolder setImageResource(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    public BaseViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    public BaseViewHolder setImageDrawable(int viewId, Drawable d) {
        ((ImageView) getView(viewId)).setImageDrawable(d);
        return this;
    }

    public BaseViewHolder setChecked(int viewId, boolean checked) {
        CheckBox checkBox = getView(viewId);
        checkBox.setChecked(checked);
        return this;
    }

    public BaseViewHolder setAlpha(int viewId, float alpha) {
        View view = getView(viewId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setAlpha(alpha);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
            alphaAnimation.setDuration(0);
            alphaAnimation.setFillAfter(true);
            view.startAnimation(alphaAnimation);
        }
        return this;
    }

    public BaseViewHolder linkify(int viewId) {
        Linkify.addLinks((TextView) getView(viewId), Linkify.ALL);
        return this;
    }

    public BaseViewHolder linkify(int viewId, int mask) {
        Linkify.addLinks((TextView) getView(viewId), mask);
        return this;
    }

    public BaseViewHolder setTypeface(int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    public BaseViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            setTypeface(viewId, typeface);
        }
        return this;
    }

    public BaseViewHolder setProgress(int viewId, int progress) {
        ((ProgressBar) getView(viewId)).setProgress(progress);
        return this;
    }

    public BaseViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar progressBar = getView(viewId);
        progressBar.setProgress(progress);
        progressBar.setMax(max);
        return this;
    }

    public BaseViewHolder setProgressMax(int viewId, int max) {
        ((ProgressBar) getView(viewId)).setMax(max);
        return this;
    }

    public BaseViewHolder setRating(int viewId, float rating) {
        ((RatingBar) getView(viewId)).setRating(rating);
        return this;
    }

    public BaseViewHolder setRating(int viewId, float rating, int max) {
        RatingBar ratingBar = getView(viewId);
        ratingBar.setRating(rating);
        ratingBar.setMax(max);
        return this;
    }

    public BaseViewHolder setTag(int viewId, Object tag) {
        getView(viewId).setTag(tag);
        return this;
    }

    public BaseViewHolder setTag(int viewId, int key, Object tag) {
        getView(viewId).setTag(key, tag);
        return this;
    }

    public BaseViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        ((CompoundButton) getView(viewId)).setOnCheckedChangeListener(listener);
        return this;
    }

    public BaseViewHolder setOnClickListener(View.OnClickListener listener) {
        getView().setOnClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnLongClickListener(View.OnLongClickListener listener) {
        getView().setOnLongClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        getView(viewId).setOnLongClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnTouchListener(View.OnTouchListener listener) {
        getView().setOnTouchListener(listener);
        return this;
    }

    public BaseViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        getView(viewId).setOnTouchListener(listener);
        return this;
    }

    public BaseViewHolder addTextChangedListener(int viewId, TextWatcher watcher){
        ((TextView) getView(viewId)).addTextChangedListener(watcher);
        return this;
    }
}
