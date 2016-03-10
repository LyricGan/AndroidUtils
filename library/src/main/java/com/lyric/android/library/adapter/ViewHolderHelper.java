package com.lyric.android.library.adapter;

/**
 * @author lyric
 * @description
 * @time 16/3/10
 */
public class ViewHolderHelper {
    private static ViewHolder sViewHolder;

    public static ViewHolder getViewHolder() {
        if (sViewHolder == null) {
            sViewHolder = new ViewHolder();
        }
        return sViewHolder;
    }
}
