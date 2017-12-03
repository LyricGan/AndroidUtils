package com.lyric.android.app.test.proxy;

import java.lang.reflect.Proxy;

public class ProxyTest {
	
	public static void main(String[] args) {
        TimeInvocationHandler handler = new TimeInvocationHandler(new OperateImpl());
        Operate operate = (Operate) Proxy.newProxyInstance(Operate.class.getClassLoader(), new Class[] {Operate.class}, handler);
        operate.method1();
        System.out.println();
        operate.method2();
        System.out.println();
        operate.method3();
	}

    interface Operate {

        void method1();

        void method2();

        void method3();
    }

    private static class OperateImpl implements Operate {

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
}
