package com.lyric.android.app.test.proxy;

import java.lang.reflect.Proxy;

public class ProxyTest {
	
	public static void main(String[] args) {
		invoke();
	}

    private static void invoke() {
        TimeInvocationHandler handler = new TimeInvocationHandler(new OperateImpl());
        Operate operate = (Operate) Proxy.newProxyInstance(Operate.class.getClassLoader(), new Class[] {Operate.class}, handler);
        operate.method1();
        System.out.println();
        operate.method2();
        System.out.println();
        operate.method3();
    }
}
