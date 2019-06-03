package com.javalemon.threadpool;

import java.util.concurrent.*;

/**
 * @author lemon
 * @date 2019-06-02
 * @desc 周期线程池
 */

public class ScheduledThreadPool {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4, new MyThreadFactory());

        System.out.println("start:" + System.currentTimeMillis());
        ScheduledFuture<?> future = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                //此处没有捕获异常将导致周期线程中断
                testException();
            }
        }, 100L, 1000L, TimeUnit.MILLISECONDS);


        try {
            Thread.sleep(15000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scheduledExecutorService.shutdown();

    }

    private static int number = 1;
    private static void testException() {
        System.out.println("time:" + System.currentTimeMillis() + ",threadName:" + Thread.currentThread().getName());
        number++;
        if (number == 5) {
            throw new RuntimeException("this a exception");
        }
    }
}
