package com.houlik.libhoulik.android.util;

import android.os.Looper;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

/**
 *
 * Thread.setDefaultUncaughtExceptionHandler(new LogExceptionHandler(new LogExceptionHandler.ExceptionHandler(){...}));
 * @author : houlik
 * @since : 2020/12/7
 * email : houlik@126.com
 * 注释 :
 */
public class LogExceptionHandler implements Thread.UncaughtExceptionHandler {

    private ExceptionHandler exceptionHandler;

    public LogExceptionHandler(ExceptionHandler exceptionHandler){
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void uncaughtException(@NonNull final Thread t, @NonNull final Throwable e) {

        StringBuffer exceptionLog = new StringBuffer();
        exceptionLog.append(e.getMessage());
        StackTraceElement[] elements = e.getStackTrace();

        for (int i = 0; i < elements.length; i++) {
            exceptionLog.append(elements[i].toString());
        }

        //发送收集到的Crash信息到服务器
        exceptionHandler.sendLogs(exceptionLog.toString());

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                //处理异常
                exceptionHandler.solveException();
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    //关闭虚拟机，释放所有内存
                    System.exit(0);
                }
                Looper.loop();
            }
        }).start();



    }

    public interface ExceptionHandler{
        //执行任务 - 发送异常日志到服务器 或者其它任务
        void sendLogs(String log);

        //解决任务 - 解决异常或者直接终止程序
        void solveException();
    }
}
