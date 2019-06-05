#线程池的创建方式
## 两种创建方式
### 一、直接实现ThreadPoolExecutor
```
new ThreadPoolExecutor(nThreads, nThreads,
                              0L, TimeUnit.MILLISECONDS,
                              new LinkedBlockingQueue<Runnable>(),
                              threadFactory);
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
6. 第六个参数： threadFactory表示线程工厂。
7. 第七个参数： handler表示执行拒绝策略的对象。
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

### 三、ThreadPoolExecutor类中的execute() 方法
通过submit()方法也可以提交任务，submit方法最终也是调用execute()方法
```
public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();
    if (poolSize >= corePoolSize || !addIfUnderCorePoolSize(command)) {
        if (runState == RUNNING && workQueue.offer(command)) {
            if (runState != RUNNING || poolSize == 0)
                ensureQueuedTaskHandled(command);
        }
        else if (!addIfUnderMaximumPoolSize(command))
            reject(command); // is shutdown or saturated
    }
}
```
1.如果当前线程池中的线程数目小于corePoolSize, 则每来一个任务，就会创建线程去执行这个任务
2.如果当前线程池中的线程数据>=corePoolSize,则每来一个任务，会尝试将其添加到任务缓存队列当中，若添加成功，则该任务会等待空闲线程将其取出去执行；若添加失败（一般来说是任务缓存队列已满），则会尝试创建新的线程去执行这个任务；
3.如果当前线程池中的线程数目达到maximumPoolSize，则会采取任务拒绝策略进行处理；
> #### 任务拒绝策略
>> ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
>> ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
>> ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
>> ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务

4.如果线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止，直至线程池中的线程数目不大于corePoolSize；如果允许为核心池中的线程设置存活时间，那么核心池中的线程空闲时间超过keepAliveTime，线程也会被终止。



