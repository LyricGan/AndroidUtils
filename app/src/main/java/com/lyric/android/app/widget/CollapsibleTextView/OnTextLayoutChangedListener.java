package com.lyric.android.app.widget.CollapsibleTextView;

/**
 * 文本状态变化监听事件
 */
public interface OnTextLayoutChangedListener {

    void onChanged(boolean firstLoad, boolean flag, boolean clicked, int status);

}
