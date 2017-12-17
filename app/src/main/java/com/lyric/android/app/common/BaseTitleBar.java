package com.lyric.android.app.common;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyric.android.app.R;

/**
 * 标题栏基类
 * @author lyricgan
 * @date 17/12/17 上午11:05
 */
public class BaseTitleBar {
    private Context mContext;
    private View mTitleView;
    private ImageView ivTitleBarLeft;
    private TextView tvTitle;

    public BaseTitleBar(View titleView) {
        ivTitleBarLeft = (ImageView) titleView.findViewById(R.id.iv_title_bar_left);
        tvTitle = (TextView) titleView.findViewById(R.id.tv_title);

        this.mContext = titleView.getContext();
        this.mTitleView = titleView;
    }

    public Context getContext() {
        return mContext;
    }

    public void setVisibility(int visibility) {
        mTitleView.setVisibility(visibility);
    }

    public void setLeftDrawable(int resId) {
        ivTitleBarLeft.setImageResource(resId);
    }

    public void setLeftClickListener(View.OnClickListener listener) {
        ivTitleBarLeft.setOnClickListener(listener);
    }

    public void setLeftVisibility(int visibility) {
        ivTitleBarLeft.setVisibility(visibility);
    }

    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    public void setTitle(int textId) {
        tvTitle.setText(textId);
    }
}
