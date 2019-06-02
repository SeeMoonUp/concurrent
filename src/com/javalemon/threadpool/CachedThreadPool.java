package com.javalemon.threadpool;

import com.javalemon.bean.ConcurrentBean;

import java.util.concurrent.*;

/**
 * @author lemon(lemon @ laowantong.cc)
 * @date 2019-06-02
 * @desc
 */

public class CachedThreadPool {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool(new MyThreadFactory());

        ConcurrentBean concurrentBean = new ConcurrentBean();
        for (int i = 0; i < 1000; i++) {

            Callable<Integer> callable = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    concurrentBean.setNumber(concurrentBean.getNumber() + 1);
//                    Thread.sleep(1000L);
                    System.out.println(Thread.currentThread().getName());
                    return concurrentBean.getNumber();
                }
            };

            Future<Integer> submit = executorService.submit(callable);

        }

        executorService.shutdown();
        while (!executorService.awaitTermination(1L, TimeUnit.MILLISECONDS)) {
            System.out.println("======="+executorService.isTerminated());
        }
        System.out.println("======="+executorService.isTerminated());
        System.out.println(concurrentBean.getNumber());
    }
}
