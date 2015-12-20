package com.lrc.baseand.view.listview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lrc.baseand.R;

/**
 * 自定义列表下拉刷新视图
 * 
 * @author lyric
 * @created 2014-1-16
 * 
 */
public class MyListViewHeader extends LinearLayout {
	private LinearLayout layout_list_header;
	private ImageView iv_header_arrow;
	private ImageView iv_header_loading;
	private TextView tv_header_hint;
	private Animation mRotateUpAnimation;
	private Animation mRotateDownAnimation;
	private final int ROTATE_ANIMATION_DURATION = 180;
	/** 初始状态 */
	public static final int STATE_NORMAL = 0;
	/** 预刷新状态 */
	public static final int STATE_READY = 1;
	/** 刷新状态 */
	public static final int STATE_REFRESHING = 2;
	/** 视图当前状态 */
	private int mCurrentState = STATE_NORMAL;

	public MyListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	public MyListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新视图高度为0
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
		layout_list_header = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_list_view_header, null);
		addView(layout_list_header, layoutParams);
		setGravity(Gravity.BOTTOM);
		iv_header_arrow = (ImageView) findViewById(R.id.iv_header_arrow);
		tv_header_hint = (TextView) findViewById(R.id.tv_header_hint);
		iv_header_loading = (ImageView) findViewById(R.id.iv_header_loading);
		final AnimationDrawable animationDrawable = (AnimationDrawable) iv_header_loading.getBackground();
		iv_header_loading.post(new Runnable() {
			
			@Override
			public void run() {
				animationDrawable.start();
			}
		});
		mRotateUpAnimation = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateUpAnimation.setDuration(ROTATE_ANIMATION_DURATION);
		mRotateUpAnimation.setFillAfter(true);
		mRotateDownAnimation = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateDownAnimation.setDuration(ROTATE_ANIMATION_DURATION);
		mRotateDownAnimation.setFillAfter(true);
	}
	
	/**
	 * 设置状态
	 * @param state
	 */
	public void setState(int state) {
		if (state == mCurrentState) {
			return;
		}
		if (state == STATE_REFRESHING) {
			// 显示进度
			iv_header_arrow.clearAnimation();
			iv_header_arrow.setVisibility(View.INVISIBLE);
			iv_header_loading.setVisibility(View.VISIBLE);
		} else {
			// 显示箭头图片
			iv_header_arrow.setVisibility(View.VISIBLE);
			iv_header_loading.setVisibility(View.INVISIBLE);
		}
		switch (state) {
		case STATE_NORMAL: {
			if (mCurrentState == STATE_READY) {
				iv_header_arrow.startAnimation(mRotateDownAnimation);
			}
			if (mCurrentState == STATE_REFRESHING) {
				iv_header_arrow.clearAnimation();
			}
			tv_header_hint.setText(R.string.list_header_hint_normal);
		}
			break;
		case STATE_READY: {
			if (mCurrentState != STATE_READY) {
				iv_header_arrow.clearAnimation();
				iv_header_arrow.startAnimation(mRotateUpAnimation);
				tv_header_hint.setText(R.string.list_header_hint_ready);
			}
		}
			break;
		case STATE_REFRESHING: {
			tv_header_hint.setText(R.string.list_header_hint_loading);
		}
			break;
		default:
		}
		mCurrentState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0) {
			height = 0;
		}
		LayoutParams layoutParams = (LayoutParams) layout_list_header.getLayoutParams();
		layoutParams.height = height;
		layout_list_header.setLayoutParams(layoutParams);
	}

	public int getVisiableHeight() {
		return layout_list_header.getHeight();
	}

}
