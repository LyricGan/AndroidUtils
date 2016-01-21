package com.lyric.android.app.entity;

/**
 * @author ganyu
 * @description 用户基础信息实体类
 * @time 2016/1/21 15:56
 */
public class UserBaseEntity extends BaseEntity {
    private String token;
    private String name;
    private int age;
    private String mobile;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
