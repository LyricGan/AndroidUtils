package com.lyric.android.app.test.span;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

/**
 * @author lyric
 * @description
 * @time 2016/5/26 15:57
 */
public class SpanTextUtils {

    public static CharSequence buildString(Context context, String action, String name, String content) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(action);
        builder.append(" ");
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new TextClickableSpan(new TextSpanClickImpl(context, 1), 0), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString);
        builder.append(" ");
        builder.append(content);
        return builder;
    }
}
