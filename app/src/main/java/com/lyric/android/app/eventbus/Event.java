package com.lyric.android.app.eventbus;

public class Event<T> implements EventType {
    /**
     * 消息类型
     */
    private int mType;
    /**
     * 消息传递实体
     */
    private T mMessage;
    /**
     * 标识参数
     */
    private int mArg1;

    public Event(int type) {
        this(type, null);
    }

    public Event(int type, T message) {
        this(type, message, 0);
    }

    public Event(int type, T message, int arg1) {
        this.mType = type;
        this.mMessage = message;
        this.mArg1 = arg1;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public T getMessage() {
        return mMessage;
    }

    public void setMessage(T message) {
        this.mMessage = message;
    }

    public int getArg1() {
        return mArg1;
    }

    public void setArg1(int arg1) {
        this.mArg1 = arg1;
    }
}