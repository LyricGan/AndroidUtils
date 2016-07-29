package com.lyric.android.app.utils;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/29 11:29
 */
public class ReflectException extends RuntimeException {
    private static final long serialVersionUID = 312038727504126519L;

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException() {
        super();
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }
}
