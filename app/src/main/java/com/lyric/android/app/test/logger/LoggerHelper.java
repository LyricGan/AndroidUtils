package com.lyric.android.app.test.logger;

/**
 * Logger is a wrapper of {@link android.util.Log} But more pretty, simple and
 * powerful
 *
 * @author Orhan Obut
 */
public final class LoggerHelper {
    private static final String DEFAULT_TAG = LoggerHelper.class.getSimpleName();
    private static final int DEFAULT_METHOD_COUNT = 2;
    private static final ILogger LOGGER = new LoggerImpl();

    // no instance
    private LoggerHelper() {
    }

    /**
     * It is used to get the settings object in order to change settings
     *
     * @return the settings object
     */
    public static LoggerSettings init() {
        return LOGGER.init(DEFAULT_TAG);
    }

    /**
     * It is used to change the tag
     *
     * @param tag is the given string which will be used in Logger
     */
    public static LoggerSettings init(String tag) {
        return LOGGER.init(tag);
    }

    public static ILogger t(String tag) {
        return LOGGER.t(tag, DEFAULT_METHOD_COUNT);
    }

    public static ILogger t(int methodCount) {
        return LOGGER.t(null, methodCount);
    }

    public static ILogger t(String tag, int methodCount) {
        return LOGGER.t(tag, methodCount);
    }

    public static void d(String message, Object... args) {
        LOGGER.d(message, args);
    }

    public static void e(String message, Object... args) {
        LOGGER.e(null, message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        LOGGER.e(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        LOGGER.i(message, args);
    }

    public static void v(String message, Object... args) {
        LOGGER.v(message, args);
    }

    public static void w(String message, Object... args) {
        LOGGER.w(message, args);
    }

    public static void wtf(String message, Object... args) {
        LOGGER.wtf(message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        LOGGER.json(json);
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        LOGGER.xml(xml);
    }
}
