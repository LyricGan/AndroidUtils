package com.lyric.android.app.common;

import android.os.Bundle;
import android.view.View;

/**
 * 基础接口类
 *
 * @author lyricgan
 * @version 2015-9-24
 */
public interface IBaseListener extends View.OnClickListener {

    /**
     * 在super.onCreate()之前调用
     * @param savedInstanceState bundles
     */
    void onPrepareCreate(Bundle savedInstanceState);

    /**
     * 初始化传递参数
     * @param bundle 传递参数
     */
    void initExtras(Bundle bundle);

    /**
     * 获取布局ID
     * @return 布局ID
     */
    int getLayoutId();

	/**
	 * 初始化布局
	 * @param view 页面视图
	 * @param savedInstanceState bundles
	 */
	void onViewInitialize(View view, Bundle savedInstanceState);

    /**
     * 初始化数据
     * @param savedInstanceState bundles
     */
    void onDataInitialize(Bundle savedInstanceState);

	/**
	 * 组件点击事件处理
	 * @param v view
	 */
	void onViewClick(View v);

    /**
     * 通过ID查找视图，泛型处理
     * @param id 视图ID
     * @param <T> 泛型，继承View
     * @return 视图或null
     */
    <T extends View> T findViewWithId(int id);
}