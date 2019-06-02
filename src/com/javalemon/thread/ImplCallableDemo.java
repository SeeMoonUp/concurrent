package com.javalemon.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author lemon
 * @date 2019-05-30
 * @desc
 */

public class ImplCallableDemo implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName());
        return "test";
    }

    public static void main(String[] args) {
        ImplCallableDemo implCallableDemo = new ImplCallableDemo();
        try {
            String call = implCallableDemo.call();
            System.out.println(call);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FutureTask<String> ft = new FutureTask<>(implCallableDemo);
        Thread thread = new Thread(ft);
        thread.start();

        String s = null;
        try {
            s = ft.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }
}
