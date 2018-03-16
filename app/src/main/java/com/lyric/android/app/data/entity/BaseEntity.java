package com.lyric.android.app.data.entity;

/**
 * @author lyricgan
 * @time 2016/8/3 11:36
 */
public class BaseEntity<T> {
    private int errorCode;
    private int errorMessage;
    private T result;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(int errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
