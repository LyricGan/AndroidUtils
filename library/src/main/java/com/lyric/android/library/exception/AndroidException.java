package com.lyric.android.library.exception;

/**
 * @author ganyu
 * @description 自定义异常类
 * @time 16/3/6 下午9:38
 */
public class AndroidException extends RuntimeException {

    public AndroidException() {
    }

    public AndroidException(String detailMessage) {
        super(detailMessage);
    }

    public AndroidException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AndroidException(Throwable throwable) {
        super(throwable);
    }

}
