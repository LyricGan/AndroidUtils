package com.lyric.android.app.test.proxy;

/**
 * @author lyricgan
 * @description
 * @time 2016/10/21 11:54
 */
public class OperateImpl implements Operate {

    @Override
    public void method1() {
        System.out.println("Invoke operateMethod1");
        sleep(110);
    }

    @Override
    public void method2() {
        System.out.println("Invoke operateMethod2");
        sleep(120);
    }

    @Override
    public void method3() {
        System.out.println("Invoke operateMethod3");
        sleep(130);
    }

    private void sleep(long timestamp) {
        try {
            Thread.sleep(timestamp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
