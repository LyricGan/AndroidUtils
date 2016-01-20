package com.lyric.android.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lyric.android.app.R;

/**
 * @author ganyu
 * @description
 * @time 2016/1/20 14:21
 */
public class TitleBar extends FrameLayout {
    private TextView tv_title;

    public TitleBar(Context context) {
        super(context);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.view_title_bar, this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }
}
