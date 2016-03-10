package com.lyric.android.library.widget.ListView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author lyric
 * @description
 * @time 16/3/10
 */
public class MultipleListView extends ListView {

    public MultipleListView(Context context) {
        super(context);
    }

    public MultipleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultipleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultipleListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
