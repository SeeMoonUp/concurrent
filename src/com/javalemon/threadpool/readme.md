#线程池的创建方式
## 两种创建方式
### 一、直接实现ThreadPoolExecutor
```
new ThreadPoolExecutor(nThreads, nThreads,
                              0L, TimeUnit.MILLISECONDS,
                              new LinkedBlockingQueue<Runnable>(),
                              threadFactory);
```

### 二、使用Executors创建5中线程池