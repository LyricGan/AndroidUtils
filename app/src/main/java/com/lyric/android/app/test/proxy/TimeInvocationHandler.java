package com.lyric.android.app.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author lyricgan
 * @description
 * @time 2016/10/9 15:31
 */
public class TimeInvocationHandler implements InvocationHandler {
    private Object mTarget;

    public TimeInvocationHandler() {
    }

    public TimeInvocationHandler(Object target) {
        this.mTarget = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.currentTimeMillis();
        Object object = method.invoke(mTarget, args);
        System.out.println(method.getName() + " cost time is:" + (System.currentTimeMillis() - start));
        return object;
    }
}
