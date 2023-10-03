package com.houlik.libhoulik.houlikapp.thread;

import java.util.concurrent.TimeUnit;

/**
 * 唯一的保持运行的线程
 * @author Houlik
 * @since 2022/11/22
 * @description 这是保持运行的唯一线程
 *
 */
public class KeepThread implements Runnable{

    private OnKeepThread onKeepThread;
    private boolean isThreadKeepRunning;

    public KeepThread(){
        createThread();
    }

    private void createThread(){
        HLThread.getInstance().useThreadPoolExecutor(this);
    }

    @Override
    public void run() {
        while (isThreadKeepRunning){

            if(onKeepThread != null){
                onKeepThread.keepRunningAction();
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnKeepThread{
        void keepRunningAction();
    }

    public void setOnKeepThread(OnKeepThread onKeepThread){
        this.onKeepThread = onKeepThread;
    }

    public void setThreadKeepRunning(boolean isThreadKeepRunning){
        this.isThreadKeepRunning = isThreadKeepRunning;
    }


}
