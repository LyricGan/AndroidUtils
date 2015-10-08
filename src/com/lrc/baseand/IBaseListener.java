package com.lrc.baseand;

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
	 * @param savedInstanceState
	 */
	void onInitView(Bundle savedInstanceState);

	/**
	 * 组件点击事件处理
	 * @param v
	 */
	void onWidgetClick(View v);
	
	/**
	 * 响应网络请求
	 * @param flag 请求标识
	 * @param response 响应字符串
	 */
	void onResponse(int flag, String response);
}
