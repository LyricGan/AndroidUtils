package com.lyric.android.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyric.android.app.R;

/**
 * @author ganyu
 * @description title bar
 * @time 2016/1/20 14:21
 */
public class TitleBar extends FrameLayout {
    private ImageView iv_title_bar_left;
    private TextView tv_title;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        View rootView = View.inflate(context, R.layout.view_title_bar, this);
        iv_title_bar_left = (ImageView) rootView.findViewById(R.id.iv_title_bar_left);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);

        tv_title.setText(R.string.app_name);
        iv_title_bar_left.setVisibility(View.GONE);
    }

    public void setText(CharSequence title) {
        tv_title.setText(title);
    }

    public void setText(int textId) {
        setText(getContext().getResources().getText(textId));
    }

    public void setLeftDrawable(int resId) {
        iv_title_bar_left.setImageResource(resId);
        iv_title_bar_left.setVisibility(View.VISIBLE);
    }

    public void setLeftClickListener(OnClickListener listener) {
        iv_title_bar_left.setOnClickListener(listener);
    }

    public void setLeftVisibility(int visibility) {
        iv_title_bar_left.setVisibility(visibility);
    }
}
