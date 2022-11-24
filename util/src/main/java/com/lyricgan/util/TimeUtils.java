package com.lyricgan.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间工具类
 * @author Lyric Gan
 */
public class TimeUtils {
    public static final long ONE_SECOND = 1000L;
    public static final long ONE_MINUTE = 60 * ONE_SECOND;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    /**
     * 时间偏移量，格式为时间戳
     */
    private static long sTimeOffset;

    private TimeUtils() {
    }

    public static boolean isToday(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(getSystemTimeZone());
        Date date = new Date(milliseconds);
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int day = calendar.get(Calendar.DAY_OF_YEAR);

        date = new Date(currentTimeMillis());
        calendar.setTime(date);
        final int todayYear = calendar.get(Calendar.YEAR);
        final int todayDay = calendar.get(Calendar.DAY_OF_YEAR);

        return year == todayYear && day == todayDay;
    }

    public static String getTime(long milliseconds, String template) {
        return getTime(milliseconds, template, getChinaLocale());
    }

    public static String getTime(long milliseconds, String template, Locale locale) {
        if (TextUtils.isEmpty(template)) {
            template = "yyyy-MM-dd";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(template, locale);
        setTimeZone(dateFormat, locale);
        return dateFormat.format(new Date(milliseconds));
    }

    /**
     * 获取转换后的时间，将一种格式转换为另一种，例如将yyyy-MM-dd HH:mm:ss转换为MM-dd HH:mm
     *
     * @param timeString 时间格式：如"yyyy-MM-dd HH:mm:ss"、"yyyyMMddHHmmss"
     * @param template1  转换前格式
     * @param template2  转换后格式
     * @return 转换后的时间
     */
    public static String getTime(String timeString, String template1, String template2) {
        if (TextUtils.isEmpty(timeString)) {
            return timeString;
        }
        if (TextUtils.isEmpty(template1)) {
            template1 = "yyyy-MM-dd HH:mm:ss";
        }
        if (TextUtils.isEmpty(template2)) {
            template1 = "MM-dd HH:mm";
        }
        long timeMilliseconds = getTimeMilliseconds(timeString, template1, getChinaLocale());
        return getTime(timeMilliseconds, template2, getChinaLocale());
    }

    /**
     * 格式化时间，转换为时间戳，使用中国时区
     */
    public static long getTimeMilliseconds(String timeString, String template) {
        return getTimeMilliseconds(timeString, template, getChinaLocale());
    }

    public static long getTimeMilliseconds(String timeString, String template, Locale locale) {
        long milliseconds = 0;
        if (TextUtils.isEmpty(timeString)) {
            return milliseconds;
        }
        if (TextUtils.isEmpty(template)) {
            template = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(template, locale);
        setTimeZone(dateFormat, locale);
        try {
            Date date = dateFormat.parse(timeString);
            if (date != null) {
                milliseconds = date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    private static Locale getChinaLocale() {
        return Locale.SIMPLIFIED_CHINESE;
    }

    private static Locale getSystemLocale() {
        return Locale.getDefault();
    }

    private static TimeZone getChinaTimeZone() {
        return TimeZone.getTimeZone("GMT+8:00");
    }

    private static TimeZone getSystemTimeZone() {
        return TimeZone.getDefault();
    }

    private static void setTimeZone(DateFormat dateFormat, Locale locale) {
        if (locale == getChinaLocale()) {
            dateFormat.setTimeZone(getChinaTimeZone());
        } else {
            dateFormat.setTimeZone(getSystemTimeZone());
        }
    }

    public static String getTimeZone() {
        try {
            TimeZone timeZone = getSystemTimeZone();
            return timeZone.getDisplayName(true, TimeZone.SHORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setTimeOffset(long timeOffset) {
        sTimeOffset = timeOffset;
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis() - sTimeOffset;
    }

    public static String formatHourMinuteSeconds(long timeSeconds) {
        long hours = timeSeconds / 3600;
        long minutes = timeSeconds % 3600 / 60;
        long seconds = timeSeconds % 60;
        String hoursStr = hours >= 10 ? "" + hours : "0" + hours;
        String minutesStr = minutes >= 10 ? "" + minutes : "0" + minutes;
        String secondsStr = seconds >= 10 ? "" + seconds : "0" + seconds;
        return hoursStr + ":" + minutesStr + ":" + secondsStr;
    }

    public static String formatMinuteSeconds(long timeSeconds) {
        long minutes = timeSeconds / 60;
        long seconds = timeSeconds % 60;
        String minutesStr = minutes >= 10 ? "" + minutes : "0" + minutes;
        String secondsStr = seconds >= 10 ? "" + seconds : "0" + seconds;
        return minutesStr + ":" + secondsStr;
    }
}
