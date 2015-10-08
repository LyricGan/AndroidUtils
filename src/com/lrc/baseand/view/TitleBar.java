package com.lrc.baseand.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lrc.baseand.R;
import com.lrc.baseand.constants.AppConstants;

/**
 * 应用标题栏，继承 {@link RelativeLayout} ，实现 {@link AppConstants} 接口
 * 
 * @author ganyu
 *
 */
public class TitleBar extends RelativeLayout implements AppConstants {
	public RelativeLayout mRelativeLayout;
	private ImageButton ibtn_back;
	private TextView tv_title;
	private TextView tv_right;
	
	/** 返回按钮ID */
	public static final int BACK_ID = 0x1000;
	/** 标题ID */
	public static final int TITLE_ID = 0x1004;
	/** 右侧文本ID */
	public static final int RIGHT_ID = 0x1012;

	public TitleBar(Context context) {
		super(context);
		init(context);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void init(Context context) {
		// 标题栏父视图
		mRelativeLayout = new RelativeLayout(context);
		mRelativeLayout.setBackgroundColor(Color.WHITE);
		RelativeLayout.LayoutParams rootParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		DisplayMetrics outMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		int screenWidth = outMetrics.widthPixels;
		int screenHeight = outMetrics.heightPixels;
		if (screenWidth < 640) {
			rootParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 60);
		} else if (screenWidth >= 640 && screenHeight <= 1280) {
			rootParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 80);
		} else if (screenWidth >= 1080 && screenHeight <= 1920) {
			rootParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 100);
		} else {
			rootParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 120);
		}
		rootParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		this.addView(mRelativeLayout, rootParams);
		
		// 返回按钮
		ibtn_back = new ImageButton(context);
		ibtn_back.setId(BACK_ID);
		ibtn_back.setPadding(0, 0, 0, 0);
		RelativeLayout.LayoutParams backParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		backParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		backParams.addRule(RelativeLayout.CENTER_VERTICAL);
		backParams.setMargins(0, 0, 0, 0);
		mRelativeLayout.addView(ibtn_back, backParams);
		
		// 标题文本
		tv_title = new TextView(context);
		tv_title.setId(TITLE_ID);
		tv_title.setTextColor(Color.parseColor("#0099CC"));
		tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, 24f);
		tv_title.setEllipsize(TruncateAt.MARQUEE);
		tv_title.setHorizontallyScrolling(true);
		tv_title.setMarqueeRepeatLimit(-1);
		tv_title.setSingleLine(true);
		RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mRelativeLayout.addView(tv_title, titleParams);
		
		// 右侧文本导航
		tv_right = new TextView(context);
		tv_right.setId(RIGHT_ID);
		tv_right.setTextColor(Color.parseColor("#0099CC"));
		tv_right.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20f);
		tv_right.setSingleLine(true);
		RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rightParams.addRule(RelativeLayout.CENTER_VERTICAL);
		mRelativeLayout.addView(tv_right, rightParams);
		
		// 默认显示状态
		ibtn_back.setVisibility(View.GONE);
		tv_title.setText(R.string.app_name);
		tv_right.setVisibility(View.GONE);
	}
	
	/**
	 * 设置标题
	 * @param titleResId 标题索引，-1代表为空
	 */
	public void setTitle(int titleResId) {
		this.setTitle(null, titleResId, false, -1, null);
	}
	
	/**
	 * 设置标题，指定返回按钮点击事件
	 * @param titleResId 标题索引，-1代表为空
	 * @param onClickListener 
	 */
	public void setTitle(int titleResId, OnClickListener onClickListener) {
		this.setTitle(null, titleResId, true, -1, onClickListener);
	}
	
	/**
	 * 设置标题
	 * @param activity
	 * @param titleResId 标题索引，-1代表为空
	 */
	public void setTitle(Activity activity, int titleResId) {
		this.setTitle(activity, titleResId, false, -1, null);
	}
	
	/**
	 * 设置标题
	 * @param activity
	 * @param titleResId 标题索引，-1代表为空
	 * @param rightResId 右侧文字索引，-1代表为空
	 * @param onClickListener null代表不设置监听事件
	 */
	public void setTitle(Activity activity, int titleResId, int rightResId, OnClickListener onClickListener) {
		this.setTitle(activity, titleResId, false, rightResId, onClickListener);
	}
	
	/**
	 * 设置标题
	 * @param activity
	 * @param titleResId 标题索引，-1代表为空
	 * @param isBackSpecified 是否指定消费返回按钮事件
	 * @param rightResId 右侧文字索引，-1代表为空
	 * @param onClickListener null代表不设置监听事件
	 */
	public void setTitle(final Activity activity, int titleResId, boolean isBackSpecified, int rightResId, OnClickListener onClickListener) {
		ibtn_back.setVisibility(View.VISIBLE);
		if (isBackSpecified && onClickListener != null) {
			ibtn_back.setOnClickListener(onClickListener);
		} else {
			if (activity != null) {
				ibtn_back.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						activity.finish();
					}
				});
			} else {
				ibtn_back.setVisibility(View.GONE);
			}
		}
		if (titleResId != -1) {
			tv_title.setText(titleResId);
		}
		if (rightResId != -1 && onClickListener != null) {
			tv_right.setVisibility(View.VISIBLE);
			tv_right.setText(rightResId);
			tv_right.setOnClickListener(onClickListener);
		} else {
			tv_right.setVisibility(View.GONE);
		}
	}
	
}
