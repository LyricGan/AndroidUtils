package com.lyricgan.demo.util.widget;

import android.util.SparseArray;
import android.view.View;

/**
 * @author Lyric Gan
 * @description
 * @since 2016/6/14 12:45
 */
public class ViewHolderHelper {

    private ViewHolderHelper() {
    }

    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
