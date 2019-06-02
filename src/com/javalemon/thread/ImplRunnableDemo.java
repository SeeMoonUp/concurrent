package com.javalemon.thread;

/**
 * @author lemon(lemon @ laowantong.cc)
 * @date 2019-05-30
 * @desc
 */

public class ImplRunnableDemo implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        System.out.println(System.currentTimeMillis());
    }

    public static void main(String[] args) {
        ImplRunnableDemo demo = new ImplRunnableDemo();
        demo.run();
        Thread thread = new Thread(demo);
        thread.start();
    }
}
