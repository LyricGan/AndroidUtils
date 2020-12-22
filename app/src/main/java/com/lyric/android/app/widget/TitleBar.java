package com.lyric.android.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyric.android.app.R;

/**
 * title bar
 *
 * @author Lyric Gan
 */
public class TitleBar extends FrameLayout {
    private ImageView ivTitleBarLeft;
    private TextView tvTitle;

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
        ivTitleBarLeft = (ImageView) rootView.findViewById(R.id.iv_title_bar_left);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
    }

    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    public void setTitle(int textId) {
        setTitle(getContext().getResources().getText(textId));
    }

    public void setLeftDrawable(int resId) {
        ivTitleBarLeft.setImageResource(resId);
        ivTitleBarLeft.setVisibility(View.VISIBLE);
    }

    public void setLeftClickListener(OnClickListener listener) {
        ivTitleBarLeft.setOnClickListener(listener);
    }

    public void setLeftVisibility(int visibility) {
        ivTitleBarLeft.setVisibility(visibility);
    }
}
