package com.javalemon.queue;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * author:WangZhaoliang
 * Date:2019/6/12 14:13
 */
public class QueueBasic {

    public static void main(String[] args) throws Exception {
        /**
         * public class LinkedList<E>
         *     extends AbstractSequentialList<E>
         *     implements List<E>, Deque<E>, Cloneable, java.io.Serializable
         *     Deque -->
         *     public interface Deque<E> extends Queue<E>
         */
        LinkedBlockingDeque<String> queue = new LinkedBlockingDeque<>(5);
        queue.add("a");
        queue.add("b");
        queue.add("c");
        queue.add("d");
        queue.add("e");
        //初始容量为5，add() 方法抛异常
        //System.out.println(queue.offer("f"));

        int size = queue.size();
        for (int i = 0; i < size; i++) {
            System.out.println(queue.poll());
        }

        printQueue(queue);


        //take put
        queue.put("aaa");
        queue.put("bbb");
        queue.put("ccc");
        queue.put("ddd");
        queue.put("eee");
        System.out.println(queue.take());//如果不取出一个元素，会一直阻塞在这
        queue.put("fff");

        printQueue(queue);
    }

    /**
     * 完全打印
     * @param queue
     */
    public static void printQueue(Queue queue) {
        if (queue == null || queue.size() == 0) {
            System.out.println("queue is null!");
            return;
        }

        queue.stream().forEach(x -> {
            System.out.println(x);
        });
    }

}
