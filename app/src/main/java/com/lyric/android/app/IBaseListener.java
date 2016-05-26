package com.lyric.android.app;

import android.os.Bundle;
import android.view.View;

/**
 * 基础接口类
 * 
 * @author ganyu
 * @version 2015-9-24
 */
public interface IBaseListener {

	/**
	 * 初始化布局界面
	 * @param savedInstanceState bundles
	 */
	void onViewCreated(Bundle savedInstanceState);

	/**
	 * 组件点击事件处理
	 * @param v view
	 */
	void onViewClick(View v);
}