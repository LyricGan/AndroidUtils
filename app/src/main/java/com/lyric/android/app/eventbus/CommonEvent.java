package com.lyric.android.app.eventbus;

/**
 * events
 * @author lyricgan
 */
public class CommonEvent {
    private int type;
    private String id;
    private Object object;

    public CommonEvent(int type) {
        this.type = type;
    }

    public CommonEvent(int type, String id) {
        this.type = type;
        this.id = id;
    }

    public CommonEvent(int type, String id, Object object) {
        this.type = type;
        this.id = id;
        this.object = object;
    }

    public CommonEvent(Object object) {
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
