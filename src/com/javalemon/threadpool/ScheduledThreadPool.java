package com.javalemon.threadpool;

import java.util.concurrent.*;

/**
 * @author lemon
 * @date 2019-06-02
 * @desc
 */

public class ScheduledThreadPool {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4, new MyThreadFactory());

        ScheduledFuture<?> future = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        }, 500L, 1L, TimeUnit.MILLISECONDS);


        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scheduledExecutorService.shutdown();

    }
}
