package com.lyric.android.app.test.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.lyric.android.app.base.BaseApplication;
import com.lyric.android.app.R;
import com.lyric.android.library.utils.ResourceUtils;

/**
 * @author lyric
 * @description 可点击的文本
 * @time 2016/2/17 14:12
 */
public class TextClickableSpan extends ClickableSpan implements View.OnClickListener {
    private final ITextSpanClickListener mListener;
    private int mPosition;

    public TextClickableSpan(ITextSpanClickListener listener, int position) {
        mListener = listener;
        mPosition = position;
    }

    @Override
    public void onClick(View widget) {
        if (mListener != null) {
            mListener.onClick(mPosition);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(ResourceUtils.getColor(BaseApplication.getContext(), R.color.color_007eff));
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
