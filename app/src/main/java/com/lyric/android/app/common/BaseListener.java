package com.lyric.android.app.common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * 基础接口类
 * @author lyricgan
 * @version 2015-9-24
 */
public interface BaseListener extends View.OnClickListener {

    /**
     * 在super.onCreate()之前调用
     * @param savedInstanceState bundles
     */
    void onPrepareCreate(Bundle savedInstanceState);

    /**
     * 初始化传递参数
     * @param bundle 传递参数
     */
    void onExtrasInitialize(Bundle bundle);

    /**
     * 获取布局ID
     * @return 布局ID
     */
    int getLayoutId();

    /**
     * 初始化标题栏
     * @param titleBar 标题栏
     * @param savedInstanceState bundles状态
     */
    void onTitleBarInitialize(BaseTitleBar titleBar, Bundle savedInstanceState);

	/**
	 * 初始化布局
	 * @param view 页面视图
	 * @param savedInstanceState bundles状态
	 */
	void onContentViewInitialize(View view, Bundle savedInstanceState);

    /**
     * 初始化数据
     * @param savedInstanceState bundles状态
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

    /**
     * 获取消息处理器
     * @return 消息处理器
     */
    Handler getHandler();

    /**
     * 处理消息回调
     * @param msg 消息实体
     */
    void handleMessage(Message msg);
}