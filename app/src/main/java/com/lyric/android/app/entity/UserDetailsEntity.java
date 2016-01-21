package com.lyric.android.app.entity;

/**
 * @author ganyu
 * @description 用户详细信息实体类
 * @time 2016/1/21 15:58
 */
public class UserDetailsEntity extends UserBaseEntity {
    private String email;
    private String intro;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
