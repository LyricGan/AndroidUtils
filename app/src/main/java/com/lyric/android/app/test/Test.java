package com.lyric.android.app.test;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/25 13:51
 */
public class Test {

    private Test() {
    }

    private static final class TestHolder {
        public static final Test INSTANCE = new Test();
    }

    public static Test getInstance() {
        return TestHolder.INSTANCE;
    }

    public void test() {
        ExecutorsTest.start();
    }
}
