package com.lyric.android.app.test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * @author lyricgan
 * @description
 * @time 2016/11/4 16:07
 */
public class TestUtils {

    public static String getDouble(double value) {
        return new BigDecimal(Double.toString(value)).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
    }

    public static String formatDecimal1(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    public static String formatDecimal2(double d) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(d);
    }

    public static String formatDecimal3(double d) {
        return String.format(Locale.getDefault(), "%.2f", d);
    }

    public static double formatDecimal4(double d) {
        return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
