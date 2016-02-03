package com.lyric.android.app.test;

/**
 * @author ganyu
 * @description
 * @time 16/1/31 下午12:08
 */
public class ExecutorSortTest extends ExecutorSuperSortTest {
    public String mName = "self";

    static {
        System.out.println("self static name");
    }

    {
        System.out.println("self name");
    }

    public ExecutorSortTest() {
        this.mName = "self.name";
    }
}
