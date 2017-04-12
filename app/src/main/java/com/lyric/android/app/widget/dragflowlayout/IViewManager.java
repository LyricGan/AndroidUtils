package com.lyric.android.app.widget.dragflowlayout;

import android.view.View;

/**
 * the view manager
 * Created by heaven7 on 2016/8/29.
 */
interface IViewManager {

    /**
     * remove view without notify observer
     * @param child the child to remove
     */
    void removeViewWithoutNotify(View child);

}
