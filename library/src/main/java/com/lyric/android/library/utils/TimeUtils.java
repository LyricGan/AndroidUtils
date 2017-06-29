package com.lyric.android.library.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 * 
 * @author ganyu
 * @created 2014-3-7
 * 
 */
public class TimeUtils {
	/** 一分钟的毫秒值 */
	public static final long ONE_MINUTE = 60 * 1000;
	/** 一小时的毫秒值 */
	public static final long ONE_HOUR = 60 * ONE_MINUTE;
	/** 一天的毫秒值 */
	public static final long ONE_DAY = 24 * ONE_HOUR;
	/** 一月的毫秒值 */
	public static final long ONE_MONTH = 30 * ONE_DAY;
	/** 一年的毫秒值 */
	public static final long ONE_YEAR = 12 * ONE_MONTH;

	/** 日期格式 {@code yyyy-MM-dd HH:mm:ss} */
	public static final String TEMPLATE_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	/** 日期格式 {@code yyyy-MM-dd} */
	public static final String TEMPLATE_DATE = "yyyy-MM-dd";
	/** 日期格式 {@code yyyy-MM-dd HH:mm} */
	public static final String TEMPLATE_TIME = "yyyy-MM-dd HH:mm";
    /** 日期格式 {@code yyyy-MM-dd E HH:mm} */
    public static final String TEMPLATE_WEEK = "yyyy-MM-dd E HH:mm";
    /** 日期格式 {@code HH:mm} */
    public static final String TEMPLATE_HH_MM = "HH:mm";
    /** 默认日期格式 {@code yyyy-MM-dd HH:mm:ss} */
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(TEMPLATE_DEFAULT, Locale.getDefault());

    private TimeUtils() {
    }
	
	/**
	 * 获取当前时间
	 * @param template 时间格式
	 * @return String
	 */
	public static String getCurrentTime(String template) {
		if (TextUtils.isEmpty(template)) {
			template = TEMPLATE_DEFAULT;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(template, Locale.getDefault());
		return dateFormat.format(new Date());
	}
	
	/**
	 * 获取时间
	 * @param milliseconds 时间戳
	 * @param template 时间格式
	 * @return String
	 */
	public static String getTime(long milliseconds, String template) {
		if (TextUtils.isEmpty(template)) {
			template = TEMPLATE_DEFAULT;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(template, Locale.getDefault());
		return dateFormat.format(new Date(milliseconds));
	}
	
	/**
	 * 获取指定时间
	 * @param milliseconds 时间戳
	 * @param template 时间格式
	 * @return String
	 */
	public static String getSpecificTime(long milliseconds, String template) {
		if (TextUtils.isEmpty(template)) {
			template = TEMPLATE_DEFAULT;
		}
		String time = null;
		long currentTime = System.currentTimeMillis();
		long deltaTime = currentTime - milliseconds;
		long timeNumber = 0;
		if (deltaTime < 0) {
			time = "";
		} else if (deltaTime < ONE_MINUTE) {
			time = "刚刚";
		} else if (deltaTime < ONE_HOUR) {
			timeNumber = deltaTime / ONE_MINUTE;
			time = timeNumber + "分钟前";
		} else if (deltaTime < ONE_DAY) {
			timeNumber = deltaTime / ONE_HOUR;
			time = timeNumber + "小时前";
		} else if (deltaTime < (ONE_DAY * 10)) {
			timeNumber = deltaTime / ONE_DAY;
			time = timeNumber + "天前";
		} else {
			time = getTime(milliseconds, template);
		}
		return time;
	}
	
	/**
	 * 获取中文星期名称
	 * @param milliseconds 毫秒
	 * @param template 日期格式
	 * @return String
	 */
	public static String getWeekCn(long milliseconds, String template) {
		String week = null;
		Calendar calendar = getCalendar(milliseconds, template);
		if (calendar != null) {
			switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                week = "星期天";
                break;
            case Calendar.MONDAY:
                week = "星期一";
                break;
            case Calendar.TUESDAY:
                week = "星期二";
                break;
            case Calendar.WEDNESDAY:
                week = "星期三";
                break;
            case Calendar.THURSDAY:
                week = "星期四";
                break;
            case Calendar.FRIDAY:
                week = "星期五";
                break;
            case Calendar.SATURDAY:
                week = "星期六";
                break;
			default:
				break;
			}
		}
		return week;
	}
	
	/**
	 * 获取英文星期名称
	 * @param milliseconds 毫秒
	 * @param template 日期格式
	 * @return String
	 */
	public static String getWeekEn(long milliseconds, String template) {
		String week = null;
		Calendar calendar = getCalendar(milliseconds, template);
		if (calendar != null) {
			switch (calendar.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
				week = "sunday";
				break;
			case Calendar.MONDAY:
				week = "monday";
				break;
			case Calendar.TUESDAY:
				week = "tuesday";
				break;
			case Calendar.WEDNESDAY:
				week = "wednesday";
				break;
			case Calendar.THURSDAY:
				week = "thursday";
				break;
			case Calendar.FRIDAY:
				week = "friday";
				break;
			case Calendar.SATURDAY:
				week = "saturday";
				break;
			default:
				break;
			}
		}
		return week;
	}
	
	/**
	 * 获取日历
	 * @param milliseconds 毫秒
	 * @param template 日期格式
	 * @return Calendar
	 */
	private static Calendar getCalendar(long milliseconds, String template) {
		if (TextUtils.isEmpty(template)) {
			template = TEMPLATE_DEFAULT;
		}
		SimpleDateFormat format = new SimpleDateFormat(template, Locale.getDefault());
		Date date = new Date(milliseconds);
		String dateString = format.format(date);
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(format.parse(dateString));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}
	
	/**
     * 将时间戳转换为字符串
     * @param timeInMillis 时间戳
     * @param dateFormat 日期格式
     * @return String
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * 将时间戳转换为固定格式的字符串, 格式 {@link #DEFAULT_DATE_FORMAT}
     * @param timeInMillis 日期格式
     * @return String
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 返回当前时间，格式为时间戳
     * @return String
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * 返回当前时间, 格式为 {@link #DEFAULT_DATE_FORMAT}
     * 
     * @return String
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * 返回当前时间，格式自定义
     * @return String
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
}
