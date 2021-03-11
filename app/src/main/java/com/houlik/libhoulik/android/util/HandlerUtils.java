package com.houlik.libhoulik.android.util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * Created by houlik on 2018/5/2.
 */

public class HandlerUtils {

    private static HandlerUtils handlerUtils = new HandlerUtils();
    private Handler handler;

    private HandlerUtils(){}

    public static HandlerUtils getInstance(){
        if(handlerUtils == null){
            new HandlerUtils();
        }
        return handlerUtils;
    }

    /**
     * 在主线程运行子线程执行更改UI的任务
     * 通过通知而开始执行
     * @param iHandlerUtils
     * @return
     */
    public Handler processHandler(final IHandlerUtils iHandlerUtils){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                iHandlerUtils.handlerMessageAction(msg);
            }
        };
        return handler;
    }

    /**
     * 这是在同一个类中使用
     * 异步线程
     *
     * 步骤1：创建HandlerThread实例对象
     * 传入参数 = 线程名字，作用 = 标记该线程
     * 步骤2：启动线程
     * 步骤3：创建工作线程Handler & 复写handleMessage（）
     * 作用：关联HandlerThread的Looper对象、实现消息处理操作 & 与 其他线程进行通信
     * 注：消息处理操作（HandlerMessage（））的执行线程 = mHandlerThread所创建的工作线程中执行
     * 步骤4：使用工作线程Handler向工作线程的消息队列发送消息
     * 在工作线程中，当消息循环时取出对应消息 & 在工作线程执行相关操作
     * 定义要发送的消息 Message msg = Message.obtain()
     * 消息的标识 msg.what = 1
     * 通过Handler发送消息到其绑定的消息队列 workHandler.sendMessage(msg)
     *
     * 步骤5：结束线程，即停止线程的消息循环 mHandlerThread.quit();
     *
     * 注意重点: 如果需要执行UI线程操作必须将该UI操作交给主线程执行
     * 在主线程建立
     * Thread mainThread = new Thread();
     * 在要传递消息的地方执行
     * Message message = Message.obtain(); 来传递执行命令
     * 当 HandlerThread 在 handlerMessageAction中获取命令
     * 在switch判断中使用 mainThread 来 post 通过 runnable 来运行UI操作
     * 这样就能通过线程来操作UI操作
     * 例子 :
     * [主线程]
     * Handler mainHandler = new Handler();
     *
     * HandlerUtils.getInstance().customHandlerThread(handlerThread, new IHandlerUtils() {
     *             @Override
     *             public void handlerAction(Handler handler, Message message) {
     *
     *             }
     *
     *             @Override
     *             public void handlerMessageAction(Message message) {
     *                 switch (message.what){
     *                     case 0:
     *                         mainHandler.post(new Runnable() {
     *                             @Override
     *                             public void run() {
     *                                 //执行UI操作
     *                             }
     *                         });
     *                         break;
     *                 }
     *
     *             }
     *         });
     *
     * new AsyncTask<Void, Void, Void>() {
     *             @Override
     *             protected Void doInBackground(Void... voids) {
     *                 //其他操作
     *                 //操作完毕后传达线程指令
     *                 Message message = Message.obtain();
     *                 message.what = 0;
     *                 handler.sendMessage(message);
     *                 return null;
     *             }
     *         }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, null);
     *
     *
     *
     * @param iHandlerUtils
     */
    public Handler customHandlerThread(final IHandlerUtils iHandlerUtils){
        HandlerThread handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        Handler workHandler = new Handler( handlerThread.getLooper() ) {
            @Override
            public void handleMessage(Message msg) {
                //消息处理
                iHandlerUtils.handlerMessageAction(msg);
            }
        };
        // 使用工作线程Handler向工作线程的消息队列发送消息
        // 在工作线程中，当消息循环时取出对应消息 & 在工作线程执行相关操作
        // 定义要发送的消息
        Message messageObtain = Message.obtain();
        //通过Handler发送消息到其绑定的消息队列
        iHandlerUtils.handlerAction(workHandler, messageObtain);
        return workHandler;
    }

    /**
     * 在子线程中操作UI
     * 这是在同一个类中使用
     * @param handler
     * @param handlerThread
     * @param onUIHandler
     * @return
     */
    public Handler runUIHandler(Handler handler, HandlerThread handlerThread, final OnUIHandler onUIHandler){
        handler = new Handler();
        handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        final Handler workHandler = new Handler( handlerThread.getLooper() ) {
            @Override
            public void handleMessage(Message msg) {
                //发送消息
                onUIHandler.sendMessage(msg);
            }
        };
        // 使用工作线程Handler向工作线程的消息队列发送消息
        // 在工作线程中，当消息循环时取出对应消息 & 在工作线程执行相关操作
        // 定义要发送的消息
        Message messageObtain = Message.obtain();
        //通过Handler发送消息到其绑定的消息队列
        onUIHandler.uiAction(handler, messageObtain);
        return workHandler;
    }

    /**
     * 异步运行
     * 在子线程中操作UI
     * 通过 Message messageObtain = Message.obtain();
     * messageObtain = ?
     * workHandler.sendMessage(messageObtain);
     * 传递消息给 workHandler
     * 再由handler来运行UI操作
     * @param onNotifyUIHandler
     * @return
     */
    public Handler runPostUIHandler(final OnNotifyUIHandler onNotifyUIHandler){
        final Handler handler = new Handler();
        HandlerThread handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        final Handler workHandler = new Handler( handlerThread.getLooper() ) {
            @Override
            public void handleMessage(Message msg) {
                //发送消息
                onNotifyUIHandler.uiAction(handler, msg);
            }
        };
        return workHandler;
    }

    public interface OnUIHandler{
        //在主线程运行子线程执行的任务
        void uiAction(Handler uiHandler, Message message);
        //发送命令到子线程
        void sendMessage(Message message);
    }

    public interface OnNotifyUIHandler{
        //在主线程运行子线程执行的任务
        void uiAction(Handler uiHandler, Message message);
    }

}
