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
	
	/**
	 * 响应网络请求
	 * @param flag 请求标识
	 * @param response 响应字符串
	 */
	void onResponse(int flag, String response);
}