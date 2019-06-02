package com.javalemon.threadpool;

import com.javalemon.bean.ConcurrentBean;

import java.util.concurrent.*;

/**
 * @author lemon(lemon @ laowantong.cc)
 * @date 2019-06-02
 * @desc
 */

public class FixedThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(100, new MyThreadFactory());

        ConcurrentBean concurrentBean = new ConcurrentBean();
        for (int i = 0; i < 1000; i++) {

            Callable<Integer> callable = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    System.out.println(Thread.currentThread().getName());
                    concurrentBean.setNumber(concurrentBean.getNumber() + 1);
                    return concurrentBean.getNumber();
                }
            };

            Future<Integer> submit = executorService.submit(callable);

        }


        executorService.shutdown();
        try {
            executorService.awaitTermination(1000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(concurrentBean.getNumber());
    }
}
