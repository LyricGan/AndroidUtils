package com.lyric.android.app.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 输入限制小数点两位过滤器
 * @author lyricgan
 */
public class DecimalInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String destValue = dest.toString();
        if (destValue.contains(".")) {
            String[] values = destValue.split("\\.");
            if (values.length > 1) {
                String rightValue = values[1];
                if (rightValue.length() > 1 && (dstart > (destValue.length() - 1))) {
                    source = "";
                }
            }
        } else if (".".equals(source)) {
            if (dstart < (destValue.length() - 2)) {
                source = "";
            }
        }
        return source;
    }
}
