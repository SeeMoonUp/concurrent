#JAVA队列（Queue）
队列是一种数据结构，支持FIFO,尾部添加，头部删除

##队列基本的增删改查
1. 增加元素：
> offer(): 队列添加元素如果队列已满直接返回false,队列未满则直接插入并返回true
> add():   对offer()方法的简单封装.如果队列已满,抛出异常new IllegalStateException("Queue full")
> put():   往队列里插入元素,如果队列已经满,则会一直等待直到队列为空插入新元素,或者线程被中断抛出异常

2.删除元素：
>remove(): 直接删除队头部的元素
>element()：对peek方法进行简单封装,如果队头元素存在则取出并不删除,如果不存在抛出异常NoSuchElementException()
>peek()：  直接取出队头的元素,并不删除
>poll()：  取出并删除队头的元素,当队列为空,返回null
>take()：  取出并删除队头的元素,当队列为空,则会一直等待直到队列有新元素可以取出,或者线程被中断抛出异常
>offer()方法一般跟pool()方法相对应, put()方法一般跟take()方法相对应

##阻塞队列和非阻塞队列
阻塞队列与普通队列的区别在于，
>当队列是空的时，从队列中获取元素的操作将会被阻塞，
>或者当队列是满时，往队列里添加元素的操作会被阻塞。
>试图从空的阻塞队列中获取元素的线程将会被阻塞，直到其他的线程往空的队列插入新的元素。
>同样，试图往已满的阻塞队列中添加新元素的线程同样也会被阻塞，直到其他的线程使队列重新变得空闲起来，如从队列中移除一个或者多个元素，或者完全清空队列.

###在多线程中都是使用阻塞队列来做线程缓存
1. LinkedBlockingQueue
```
public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }
```
2. SynchronousQueue
```
public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }
```

