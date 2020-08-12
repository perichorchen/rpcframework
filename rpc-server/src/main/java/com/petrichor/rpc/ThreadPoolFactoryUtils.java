package com.petrichor.rpc;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author petrichor
 * @date 2020/8/12 16:05
 * 这是一种公式的估算值
 * 如果是IO密集型应用，则线程池大小设置为2N+1；
 * 如果是CPU密集型应用，则线程池大小设置为N+1；
 * N代表CPU的核数。
 *
 * 假设我的服务器是4核的，且一般进行大数据运算，cpu消耗较大，那么线程池数量设置为5为最优。
 * （现在很多项目线程池滥用，注意分配线程数量，建议不要动态创建线程池，尽量将线程池配置在
 * 配置文件中，这样方便以后整体的把控和后期维护。每个核心业务线程池要互相独立，互不影响。）
 */

@Slf4j
public class ThreadPoolFactoryUtils {
    /**
     * 一个场景运用一个线程池
     * 线程池数量有限
     */
    private static Map<String , ExecutorService> threadPoolMap = new ConcurrentHashMap<>();

    private ThreadPoolFactoryUtils() {
    }


    //overLoad
    public static ExecutorService createCustomThreadPoolIfAbsent(CustomThreadPoolConfig customThreadPoolConfig, String threadNamePrefix, Boolean daemon) {
        //一个业务对应一个线程池，从concurrenthashmap里取；
        ExecutorService threadpool = threadPoolMap.get(threadNamePrefix);

        //这个方法主要负责清除无用的线程池
        if(threadpool.isShutdown()||threadpool.isTerminated()){
            threadPoolMap.remove(threadNamePrefix);
            threadpool = createThreadPool(customThreadPoolConfig,daemon);
            threadPoolMap.put(threadNamePrefix,threadpool);
        }
        return threadpool;
    }


//拒绝策略：
//    CallerRunsPolicy（调用者运行策略）
//    AbortPolicy（中止策略）当触发拒绝策略时，直接抛出拒绝执行的异常，中止策略的意思也就是打断当前执行流程
//    DiscardPolicy直接丢弃任务，什么都不处理，悄无声息
//    DiscardOldestPolicy丢弃最旧的任务，再去execute本次任务
    public static ExecutorService createThreadPool(CustomThreadPoolConfig customThreadPoolConfig, Boolean daemon)
    {
        //创建线程池
        return new ThreadPoolExecutor(
                customThreadPoolConfig.getCorePoolSize(),
                customThreadPoolConfig.getMaxPoolSize(),
                customThreadPoolConfig.getKeepAliveTime(),
                customThreadPoolConfig.getTimeUnit(),
                customThreadPoolConfig.getBlockingQueue(),
                new ThreadFactoryBuilder().setDaemon(daemon).build(),
                customThreadPoolConfig.getRejectedExecutionHandler()
        );
    }

    /**
     * 打印线程池的状态
     *
     * @param threadPool 线程池对象
     */
    public static void printThreadPoolStatus(ThreadPoolExecutor threadPool) {
        //定时执行线程信息打印
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.info("============ThreadPool Status=============");
            log.info("ThreadPool Size: [{}]", threadPool.getPoolSize());
            log.info("Active Threads: [{}]", threadPool.getActiveCount());
            log.info("Number of Tasks : [{}]", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());
            log.info("===========================================");
        }, 0, 1, TimeUnit.SECONDS);
    }

}
