package com.lyric.android.app.test;

/**
 * @author ganyu
 * @description
 * @time 16/1/31 下午12:08
 */
public class ExecutorSuperSortTest {
    public String mName = "super";

    static {
        System.out.println("super static name");
    }

    {
        System.out.println("super name");
    }

    public ExecutorSuperSortTest() {
        this.mName = "super.name";
    }
}
