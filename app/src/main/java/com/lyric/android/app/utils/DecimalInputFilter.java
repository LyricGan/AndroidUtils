package com.lyric.android.app.utils;

import android.text.InputFilter;
import android.text.Spanned;

import com.lyric.android.library.logger.Loggers;

/**
 * @author lyricgan
 * @description 输入限制小数点两位
 * @time 2016/10/27 10:57
 */
public class DecimalInputFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String destValue = dest.toString();
        Loggers.e("source:" + source + ",destValue:" + destValue +",start:" + start + ",end:" + end + ",dest:" + dest.toString() + ",dstart:" + dstart + ",dend:" + dend);
        // 限制小数点后只能输入两位
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
