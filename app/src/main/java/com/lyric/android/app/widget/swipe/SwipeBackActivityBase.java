package com.lyric.android.app.widget.swipe;

/**
 * 滑动关闭接口
 * 
 * @author lyricgan
 *
 */
public interface SwipeBackActivityBase {

	/**
	 * 获取和Activity相关联的SwipeBackLayout
	 * @see {@link SwipeBackLayout}
	 * @return
	 */
    SwipeBackLayout getSwipeBackLayout();
    
    /**
     * 设置是否滑动关闭
     * @param enable
     */
    void setSwipeBackEnable(boolean enable);

    /**
     * 滑动关闭Activity
     */
    void finishActivity();

}
