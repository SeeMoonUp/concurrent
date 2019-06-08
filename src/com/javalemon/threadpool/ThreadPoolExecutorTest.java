package com.javalemon.threadpool;

import java.util.concurrent.*;

/**
 * author:WangZhaoliang
 * Date:2019/6/5 14:16
 */
public class ThreadPoolExecutorTest {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                10,
                200,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(5)
        );
        for (int i = 0; i < 15; i++) {
            ThreadTask myTask = new ThreadTask(i);
            executor.execute(myTask);
            LinkedBlockingQueue<Runnable> queue = (LinkedBlockingQueue<Runnable>) executor.getQueue();
            System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                    queue.size() + "，已执行玩别的任务数目：" + executor.getCompletedTaskCount());
            if (queue.size() > 0) {

                System.out.println(queue.toString());
            }
        }
        executor.shutdown();
    }

    /**
     * 线程任务
     */
    static class ThreadTask implements Runnable{

        private Integer order;

        public ThreadTask(int order) {
            this.order = order;
        }

        @Override
        public void run() {
//            System.out.println("当前执行线程的次序：" + order);
            try {
                Thread.sleep(12000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("当前执行的线程的次序：" + order + ";执行完成");
        }

        @Override
        public String toString() {
            return order + "";
        }
    }

}
