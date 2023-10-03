package com.houlik.libhoulik.houlikapp.thread;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @author : houlikapp
 * @desription : 线程类入口
 * @email : houlik@126.com
 * @since : 10/3/2023
 */
public class HLThread {

    private static HLThread wInstance = new HLThread();

    private Handler wWorkHandler = null;
    private HandlerThread wWorkThread = null;
    private Handler wUIHandler = null;
    private KeepThread keepThread;

    private HLThreadPoolExecutor threadPoolExecutor = new HLThreadPoolExecutor();

    private HLThread(){}

    public static HLThread getInstance(){
        if(wInstance == null){
            wInstance = new HLThread();
        }
        return wInstance;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 线程操作
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 线程池获取线程
     * @param runnable
     */
    public void useThreadPoolExecutor(Runnable runnable) {
        threadPoolExecutor.getThreadPoolExecutor().execute(runnable);
    }

    /**
     * 获取主线程Looper
     */
    private Looper getWorkLooper() {
        if (wWorkThread == null) {
            wWorkThread = new HandlerThread("Rtcclient_WorkThread");
            wWorkThread.start();
        }
        return wWorkThread.getLooper();
    }

    /**
     * 如果操作的handler是空的就重新创建
     * @return
     */
    private Handler getWorkHandler() {
        return (wWorkHandler != null) ? (wWorkHandler) : (wWorkHandler = new Handler(getWorkLooper()));
    }

    /**
     * 运行操作线程
     * @param r
     * @return
     */
    public static boolean post2WorkRunnable(Runnable r) {
        return (wInstance != null) ? wInstance.getWorkHandler().post(r) : false;
    }

    public Handler getUiHandler(Context context) {
        return (wUIHandler != null) ? (wUIHandler) : (wUIHandler = new Handler(context.getMainLooper()));
    }

    public KeepThread getHlKeepThread() {
        return keepThread;
    }
}
