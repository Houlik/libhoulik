package com.houlik.libhoulik.android.util;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 子线程 定时器操作
 * Created by houlik on 2018/6/8.
 */

public class AutoUtils {

    private Timer timer;
    private TimerTask timerTask;
    private Message[] messages;
    private Handler handler;

    public AutoUtils(){}

    /**
     * 每隔一段时间重复执行 相同 或者 不同 的操作
     * @param iAutoTimer
     * @param timeDelay
     * @param timePeriod
     */
    public void startTimerRepeat(IAutoTimer iAutoTimer, int timeDelay,int timePeriod){
        timer = iAutoTimer.setTimer();
        messages = iAutoTimer.setMessage();
        handler = iAutoTimer.setHandler();
        timerTask = iAutoTimer.setTimerTask(messages,handler);
        timer.schedule(timerTask,timeDelay,timePeriod);
    }

    public void removeTimer(){
        timer.cancel();
    }

    public interface IAutoTimer{
        /**
         * 初始化
         * new Timer();
         */
        Timer setTimer();

        /**
         * 初始化长度,不需要赋值
         * new Message[长度];
         * 长度表示发送多少条指令
         */
        Message[] setMessage();

        /**
         * 在run里当循环message数组时每一个message赋值或者发送前都必须实例化一次,否则将抛出 [已发同样消息] 的异常错误
         * 如：for (int i = 0; i < message.length; i++) {
         *          message[i] = new Message(); 初始化一次
         *          message[i].what = 赋值对象[i]; 开始赋值
         *          handler.sendMessage(message[i]); 发送消息
         *    }
         *
         * @param message 设置 Message.what = ? 什么信息给 Handler
         * @param handler 通过 Handler.sendMessage(Message) 发送信息
         * @return
         */
        TimerTask setTimerTask(final Message[] message, Handler handler);

        /**
         * 通过 CallBack 得到 Message.What 来执行相关任务
         * 如: new Handler(){
         *      @Override
         *      public void handleMessage(Message msg) {
         *              switch (msg.what){
         *                  case 消息1:
         *                      do something here;
         *                      break;
         *                  case 消息2:
         *                      do something here'
         *                      break;
         *              }
         *      }
         *    }
         * @return
         */
        Handler setHandler();
    }

    public Timer getTimer(){
        return timer;
    }
}
