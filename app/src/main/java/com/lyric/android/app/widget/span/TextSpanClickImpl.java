package com.lyric.android.app.widget.span;

import android.content.Context;

import com.lyric.android.app.BaseFragmentActivity;
import com.lyric.android.app.fragment.SpannableFragment;
import com.lyric.android.library.utils.ActivityUtils;

/**
 * @author lyric
 * @description 文本点击实现类
 * @time 2016/4/21 13:19
 */
public class TextSpanClickImpl implements ITextSpanClickListener {
    private Context mContext;
    private int mType;

    public TextSpanClickImpl(Context context, int type) {
        this.mContext = context;
        this.mType = type;
    }

    @Override
    public void onClick(int position) {
        if (1 == mType) {
            ActivityUtils.startActivity(mContext, BaseFragmentActivity.newIntent(mContext, SpannableFragment.class));
        }
    }
}
