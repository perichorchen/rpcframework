package com.petrichor.rpc;

import jdk.internal.dynalink.beans.StaticClass;
import lombok.Data;

import java.util.concurrent.*;

/**
 * @author petrichor
 * @date 2020/8/12 16:14
 */


@Data
public class CustomThreadPoolConfig {
    /**
     * 线程池默认参数
     */
    private static final int DEFAULT_CORE_POOL_SIZE = 10;
    private static final int DEFAULT_MAXIMUM_POOL_SIZE_SIZE = 100;
    private static final int DEFAULT_KEEP_ALIVE_TIME = 1;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;
    private static final int DEFAULT_BLOCKING_QUEUE_CAPACITY = 100;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    /**
     * 线程池可配置参数
     */
    private  int corePoolSize=DEFAULT_CORE_POOL_SIZE;
    private  int maxPoolSize=DEFAULT_MAXIMUM_POOL_SIZE_SIZE;
    private  int keepAliveTime = DEFAULT_KEEP_ALIVE_TIME;
    private  TimeUnit timeUnit = DEFAULT_TIME_UNIT;
    private BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
    private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
}
