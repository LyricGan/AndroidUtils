package com.lrc.baseand.view.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * 自定义ScrollView，可监听ScrollView滑动到顶部和底部，通过
 * {@link #setOnBorderListener(OnBorderListener)} 进行设置
 * 
 * @author ganyu
 * @version 2015-9-6
 */
public class BorderScrollView extends ScrollView {
	private OnBorderListener mOnBorderListener;
	private View mContentView;

	public BorderScrollView(Context context) {
		super(context);
	}

	public BorderScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BorderScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		doOnBorderListener();
	}
	
	/**
	 * 设置滑动监听事件
	 * @param onBorderListener
	 */
	public void setOnBorderListener(final OnBorderListener onBorderListener) {
		this.mOnBorderListener = onBorderListener;
		if (onBorderListener == null) {
			return;
		}
		if (mContentView == null) {
			mContentView = getChildAt(0);
		}
	}

	private void doOnBorderListener() {
		if (mContentView != null && mContentView.getMeasuredHeight() <= getScrollY() + getHeight()) {
			if (mOnBorderListener != null) {
				mOnBorderListener.onBottom();
			}
		} else if (getScrollY() == 0) {
			if (mOnBorderListener != null) {
				mOnBorderListener.onTop();
			}
		}
	}
	
	/**
	 * 滑动监听接口
	 */
	public static interface OnBorderListener {

		/**
		 * 滑动到底部时调用
		 */
		public void onBottom();

		/**
		 * 滑动到顶部时调用
		 */
		public void onTop();
	}
}
