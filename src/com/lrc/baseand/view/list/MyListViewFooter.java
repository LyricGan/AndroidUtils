package com.lrc.baseand.view.list;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lrc.baseand.R;

/**
 * 自定义列表底部视图
 * 
 * @author lyric
 * @created 2014-1-16
 * 
 */
public class MyListViewFooter extends LinearLayout {
	private Context mContext;
	private View view_list_footer;
	private ImageView iv_footer_loading;
	private TextView tv_footer_hint;
	/** 初始状态 */
	public static final int STATE_NORMAL = 0;
	/** 预加载状态 */
	public static final int STATE_READY = 1;
	/** 加载状态 */
	public static final int STATE_LOADING = 2;

	public MyListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public MyListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		this.mContext = context;
		LinearLayout view_list_view_footer = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.view_list_view_footer, null);
		addView(view_list_view_footer);
		view_list_view_footer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		view_list_footer = view_list_view_footer.findViewById(R.id.layout_footer_content);
		iv_footer_loading = (ImageView) view_list_view_footer.findViewById(R.id.iv_footer_loading);
		final AnimationDrawable animationDrawable = (AnimationDrawable) iv_footer_loading.getBackground();
		iv_footer_loading.post(new Runnable() {
			
			@Override
			public void run() {
				animationDrawable.start();
			}
		});
		tv_footer_hint = (TextView) view_list_view_footer.findViewById(R.id.tv_footer_hint);
	}
	
	/**
	 * 设置状态
	 * @param state
	 */
	public void setState(int state) {
		iv_footer_loading.setVisibility(View.GONE);
		if (state == STATE_READY) {
			tv_footer_hint.setText(R.string.list_footer_hint_ready);
		} else if (state == STATE_LOADING) {
			iv_footer_loading.setVisibility(View.VISIBLE);
			tv_footer_hint.setText(R.string.list_footer_hint_loading);
		} else {
			tv_footer_hint.setText(R.string.list_footer_hint_normal);
		}
	}

	public void setBottomMargin(int height) {
		if (height < 0) {
			return;
		}
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view_list_footer.getLayoutParams();
		layoutParams.bottomMargin = height;
		view_list_footer.setLayoutParams(layoutParams);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view_list_footer.getLayoutParams();
		return layoutParams.bottomMargin;
	}

	/**
	 * 初始状态
	 */
	public void normal() {
		tv_footer_hint.setVisibility(View.VISIBLE);
		iv_footer_loading.setVisibility(View.GONE);
	}

	/**
	 * 正在加载状态
	 */
	public void loading() {
		tv_footer_hint.setVisibility(View.VISIBLE);
		iv_footer_loading.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏底部视图
	 */
	public void hide() {
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view_list_footer.getLayoutParams();
		layoutParams.height = 0;
		view_list_footer.setLayoutParams(layoutParams);
	}

	/**
	 * 显示底部视图
	 */
	public void show() {
		LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view_list_footer.getLayoutParams();
		layoutParams.height = LayoutParams.WRAP_CONTENT;
		view_list_footer.setLayoutParams(layoutParams);
	}

}
