package com.lyric.android.app.test.logger;

/**
 * @author Orhan Obut
 */
public final class LoggerSettings {
    private int methodCount = 2;
    private boolean showThreadInfo = true;
    /**
     * Determines how logs will printed
     */
    private LoggerLevel loggerLevel = LoggerLevel.FULL;

    public LoggerSettings hideThreadInfo() {
        showThreadInfo = false;
        return this;
    }

    public LoggerSettings setMethodCount(int methodCount) {
        this.methodCount = methodCount;
        return this;
    }

    public LoggerSettings setLoggerLevel(LoggerLevel loggerLevel) {
        this.loggerLevel = loggerLevel;
        return this;
    }

    public int getMethodCount() {
        return methodCount;
    }

    public boolean isShowThreadInfo() {
        return showThreadInfo;
    }

    public LoggerLevel getLoggerLevel() {
        return loggerLevel;
    }
}
