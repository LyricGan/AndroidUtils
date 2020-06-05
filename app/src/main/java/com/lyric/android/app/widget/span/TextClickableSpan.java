package com.lyric.android.app.widget.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * 可点击文本
 * @author lyricgan
 */
public class TextClickableSpan extends ClickableSpan {
    private int mColor;
    private View.OnClickListener mListener;

    public TextClickableSpan(int color, View.OnClickListener listener) {
        this.mColor = color;
        this.mListener = listener;
    }

    @Override
    public void onClick(View widget) {
        if (mListener != null) {
            mListener.onClick(widget);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(mColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
