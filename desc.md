1 进程
    进程是程序的一次执行过程，是系统运行程序的基本单位，因此进程是动态的。系统运行一个程序即是一个进程从创建，运行到消亡的过程。
   线程：
    线程与进程相似，但线程是一个比进程更小的执行单位。一个进程在其执行的过程中可以产生多个线程。与进程不同的是同类的多个线程共享同一块内存空间和一组系统资源，所以系统在产生一个线程，或是在各个线程之间作切换工作时，负担要比进程小得多
     区别：
    线程是进程划分成的更小的运行单位。线程和进程最大的不同在于基本上各进程是独立的，而各线程则不一定，因为同一进程中的线程极有可能会相互影响。
2  创建线程
继承Thread类
public class MyThread extends Thread {
	@Override
	public void run() {
		super.run();
		System.out.println("MyThread");
	}
}
public class Run { 
    public static void main(String[] args) { 
    MyThread mythread = new MyThread(); 
    mythread.start();
    System.out.println("运行结束"); 
   }
}
实现Runnable接口
public class MyRunnable implements Runnable {
	@Override
	public void run() {
		System.out.println("MyRunnable");
	}
}
public class Run {
	public static void main(String[] args) {
		Runnable runnable=new MyRunnable();
		Thread thread=new Thread(runnable);
		thread.start();
		System.out.println("运行结束！");
	}
}




3 线程池：
    线程是不能够重复启动的，创建或销毁线程存在一定的开销，所以利用线程池技术来提高系统资源利用效率
线程是稀缺资源，不能频繁的创建。
解耦作用；线程的创建于执行完全分开，方便维护。
应当将其放入一个池子中，可以给其他任务进行复用。
    
常见的创建线程池方式有以下几种：
Executors.newCachedThreadPool()：无限线程池。
Executors.newFixedThreadPool(nThreads)：创建固定大小的线程池。
Executors.newSingleThreadExecutor()：创建单个线程的线程池。

    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }

都是利用了ThreadPoolExecutor实现的

Executors提供了5种不同的线程池创建配置：
ThreadPoolExecutor
corePoolSize:核心线程数，长期驻留的线程数目；
maximumPoolSize：线程不够时能够创建的最大线程数
keepAliveTime和TimeUnit，这两个参数指定了额外的线程能够闲置多久
workQueue：工作队列，必须是BlockingQueue
execute 是如何工作的
ctl即表示线程池状态，又表示工作线程数目
public void execute(Runnable command) {
…
	int c = ctl.get();
// 检查工作线程数目，低于 corePoolSize 则添加 Worker
	if (workerCountOf(c) < corePoolSize) {
    	if (addWorker(command, true))
        	return;
    	c = ctl.get();
	}
// isRunning 就是检查线程池是否被 shutdown
// 工作队列可能是有界的，offer 是比较友好的入队方式
	if (isRunning(c) && workQueue.offer(command)) {
    	int recheck = ctl.get();
// 再次进行防御性检查
    	if (! isRunning(recheck) && remove(command))
        	reject(command);
    	else if (workerCountOf(recheck) == 0)
        	addWorker(null, false);
	}
// 尝试添加一个 worker，如果失败意味着已经饱和或者被 shutdown 了
	else if (!addWorker(command, false))
    	reject(command);
}


处理过程：
    首先创建一个线程池，然后根据任务的数量逐步将线程增大到corePoolSize，如果此时仍有任务增加，则放置到workQueue中，直到workQueue爆满为止，然后继续增加池中的线程数量（增强处理能力），最终达到maxinumPoolSize。那如果此时还有任务要增加进来呢？这就需要handler来处理了，或者丢弃新任务，或者拒绝新任务，或者挤占已有的任务。
易出现问题：
    1）避免任务堆积，如果工作线程数据太少，导致处理更不上入队的速度，这就很有可能占用大量系统内存，甚至出现OOM；
    2）避免过度扩展线程：创建线程池的时候，并不能准确预计任务压力有多大，数据特征是什么样子，很难设定一个线程数目
线程池大小的选择策略：
普适的规则和思路：
    1）主要的任务是计算:cpu的处理能力是稀缺的资源，此时大量增加线程数并不能提高计算能力，这种情况下通常建议按照cpu核数N或者N+1
    2）如果是需要较多等待的任务，例如I/O操作较多，可以参考：
线程数 = CPU 核数 × 目标 CPU 利用率 ×（1 + 平均等待时间 / 平均工作时间）

最多有多少个任务能并发执行？
线程池中的线程会不断从workQueue中取任务来执行，如果没任务可执行，则线程处于空闲状态。
在ThreadPoolExecutor中有两个参数corePoolSize和maximumPoolSize，前者被称为基本大小，表示一个线程池初始化时，里面应该有的一定数量的线程。但是默认情况下，ThreadPoolExecutor在初始化是并不会马上创建corePoolSize个线程对象，它使用的是懒加载模式。
当线程数小于corePoolSize时,提交一个任务创建一个线程(即使这时有空闲线程)来执行该任务。
当线程数大于等于corePoolSize，首选将任务添加等待队列workQueue中（这里的workQueue是上面的BlockingQueue），等有空闲线程时，让空闲线程从队列中取任务。
当等待队列满时，如果线程数量小于maximumPoolSize则创建新的线程，否则使用拒绝线程处理器来处理提交的任务。
最多有多少的任务等待执行？
这个问题和BlockingQueue相关。 BlockingQueue有三个子类，一个是ArrayBlockingQueue(有界队列),一个是LinkedBlockingQueue(默认无界，但可以配置为有界)，PriorityBlockingQueue(默认无界，可配置为有界)。所以，对于有多少个任务等待执行与传入的阻塞队列有关。
newFixedThreadPool和newSingleThreadExector使用的是LinkedBlockingQueue的无界模式。而newCachedThreadPool使用的是SynchronousQueue，这种情况下线程是不需要排队等待的，SynchronousQueue适用于线程池规模无界。
如果系统过载则需要拒绝一个任务，如何通知任务被拒绝？
当有界队列被填满或者某个任务被提交到一个已关闭的Executor时将会启动饱和策略，即使用RejectedExecutionHandler来处理。JDK中提供了几种不同的RejectedExecutionHandler的实现：AbortPolicy，CallerRunsPolicy, DiscardPolicy和DiscardOldestPolicy。
AbortPolicy：默认的饱和策略。该策略将抛出未检查的RejectedExcutionException,调用者可以捕获这个异常，然后根据自己的需求来处理。
DiscardPolicy：该策略将会抛弃提交的任务
DiscardOldestPolicy：该策略将会抛弃下一个将被执行的任务(处于队头的任务)，然后尝试重新提交该任务到等待队列
CallerRunsPolicy:该策略既不会抛弃任务也不会抛出异常，而是在调用execute()的线程中运行任务。比如我们在主线程中调用了execute(task)方法，但是这时workQueue已经满了，并且也不会创建的新的线程了。这时候将会在主线程中直接运行execute中的task。