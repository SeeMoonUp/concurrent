package com.javalemon.thread;

/**
 * @author lemon
 * @date 2019-05-30
 * @desc
 */

public class ExtendThreadDemo extends Thread {

    private static int a = 1;

    @Override
    public void run() {
        super.run();
        a = a+1;
        System.out.println(System.currentTimeMillis());
    }

    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            ExtendThreadDemo etd = new ExtendThreadDemo();
            etd.start();
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(ExtendThreadDemo.a);
    }
}
