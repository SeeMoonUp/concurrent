#线程池的创建方式
## 两种创建方式
### 一、直接实现ThreadPoolExecutor
```
new ThreadPoolExecutor(int corePoolSize,
                     int maximumPoolSize,
                     long keepAliveTime,
                     TimeUnit unit,
                     BlockingQueue<Runnable> workQueue,
                     ThreadFactory threadFactory,
                     RejectedExecutionHandler handler);
```
#### 参数的含义
1. 第一个参数corePoolSize 表示常驻核心线程数。
如果等于0，则任务执行完之后，没有任何请求进入时销毁线程池的进程；
如果大于0，即使本地任务执行完毕，核心线程也不会被销毁。
这个值的设置非常关键，设置过大会浪费资源，设置过小会导致线程频繁地创建或销毁。
2. 第二个参数：maximumPoolSize 表示线程池能够容纳同时执行的最大线程数。
必须大于等于1，如果待执行的线程数大于此值，需要借助第5个参数的帮助，缓存在队列中。
如果maximumPoolSize与corePoolSize相等，即是固定大小线程池。
3. 第三个参数： keepAliveTime 表示线程池中的线程空闲时间，当空闲时间达到keepAliveTime值时，线程会被销毁，
直到只剩下corePoolSize核心线程为止，避免浪费内存和句柄资源。
在默认情况下，当线程池的线程数大于corePoolSize时，keepAliveTime才会起作用。
但是当ThreadPoolExecutor的allowCoreThreadTimeOut变量设置为true时，核心线程超时后也会被回收。
4. 第四个参数： TimeUnit表示时间单位。keepAliveTime的时间单位通常是TimeUnit.SECONDS。
5. 第五个参数： workQueue表示缓存队列。当请求的线程数大于corePoolSize时，线程进入BlockingQueue阻塞队列。
LinkedBlockingQueue是单向链表，使用锁来控制入队和出队的原子性，两个锁分别控制元素的添加和获取，是一个生产消费模型队列。
6. 第六个参数： threadFactory表示线程工厂。它用来生产一组相同任务的线程。线程池的命名是通过这个factory增加组名前缀来实现的。
在虚拟机栈分析是，就可以知道线程任务是由哪个线程工厂产生的。
7. 第七个参数： handler表示执行拒绝策略的对象。当第五个参数workQueue的任务缓存区达到上限后，并且活动线程数大于maximumPoolSize的时候，线程池通过该策略处理请求，这是一种简单的限流保护。
### 二、使用Executors创建5中线程池
#### 周期线程池
``` 
public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit);
```
第一个参数 command为需要执行的命令
第二个参数 initialDelay 为执行第一次前的延时时间
第三个参数 delay为前一次任务执行完毕，等待多久执行下一次任务
第四个参数 TimeUnit为第二个和第三个参数的时间单位
特别注意：
运行期间command抛出异常将导致整个周期线程中断，使用过程中需要特别注意这一点

