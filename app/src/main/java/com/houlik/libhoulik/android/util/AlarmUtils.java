package com.houlik.libhoulik.android.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * 定时器闹钟
 * Created by Houlik on 2018-03-16.
 */

public class AlarmUtils {

    private static AlarmUtils alarmUtils = new AlarmUtils();
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private AlarmUtils(){}

    public static AlarmUtils getInstance(){
        if(alarmUtils == null){
            new AlarmUtils();
        }
        return alarmUtils;
    }
    /**
     * 启动ALARM监听 启动服务service
     * @param service
     * @param context
     * @param tClass
     * @param alarmDuration
     */
    public void alarmRestart(Service service, Context context, Class<?> tClass, int alarmDuration){
        //得到系统的Alarm Service
        alarmManager = (AlarmManager) service.getSystemService(Context.ALARM_SERVICE);
        //要执行的Service Class
        Intent alarmIntent = new Intent(context, tClass);
        //等待执行的Intent
        pendingIntent = PendingIntent.getService(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //交给AlarmManager执行任务
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + alarmDuration, pendingIntent);
    }

    /**
     * 停止Alarm监听
     */
    public void alarmStop(){
        alarmManager.cancel(pendingIntent);
    }
}
