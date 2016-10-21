package com.lyric.android.app.test.convert;

/**
 * @author lyricgan
 * @description
 * @time 2016/10/21 16:05
 */
public class Repository {
    private int id;
    private String name;
    private User user;

    public Repository() {
    }

    public Repository(int id, String name, User user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
