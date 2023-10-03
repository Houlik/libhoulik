package com.houlik.libhoulik.houlikapp.thread;

import com.houlik.libhoulik.android.util.HLLog;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池 - 善用线程
 * @author Houlik
 * @since 2022/11/5
 * @description 依据设备可用线程创建线程池
 *
 */
public class HLThreadPoolExecutor {

    private final String TAG = "HLThreadPoolExecutor";
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private int corePoolSize = NUMBER_OF_CORES * 2;
    private int maximumPoolSize = NUMBER_OF_CORES * 2;
    private long keepAliveTime = 1;
    private int capacity = 100;

    public HLThreadPoolExecutor(){
        HLLog.i(TAG, "线程数 : " + corePoolSize);
    }

    /**
     * FixedThreadPool() 固定线程池
     * CachedThreadPool() 根据实际情况决定停止线程 - 默认60秒
     * SingleThreadExecutor() 只有一条线程的线程池, 需要等待
     * ScheduledThreadPool() 可控的线程池
     *
     * 核心线程数，除非allowCoreThreadTimeOut被设置为true，否则它闲着也不会死
     * int corePoolSize,
     *
     * 最大线程数，活动线程数量超过它，后续任务就会排队
     * int maximumPoolSize,
     *
     * 超时时长，作用于非核心线程(allowCoreThreadTimeOut被设置为true时也会同时作用于核心线程)，闲置超时便被回收
     * long keepAliveTime,
     *
     * 枚举类型，设置keepAliveTime的单位，有TimeUnit.MILLISECONDS(ms)、TimeUnit. SECONDS(s)等
     * TimeUnit unit,
     *
     * 缓冲任务队列，线程池的execute方法会将Runnable对象存储起来
     * BlockingQueue workQueue,
     *
     * 线程工厂接口，只有一个new Thread(Runnable r)方法，可为线程池创建新线程
     * ThreadFactory threadFactory)
     */
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque(capacity));

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }
}
