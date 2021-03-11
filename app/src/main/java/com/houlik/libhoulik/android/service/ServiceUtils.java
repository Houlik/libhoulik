package com.houlik.libhoulik.android.service;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

/**
 * Service工具类
 * Created by Houlik on 2017/9/29.
 */

public class ServiceUtils {

    private static final String TAG = "SERVICE UTIL";

    private static ServiceUtils serviceUtils = new ServiceUtils();

    private ServiceUtils(){}

    public static ServiceUtils getInstance(){
        if(serviceUtils == null){
            new ServiceUtils();
        }
        return serviceUtils;
    }

    /**
     * 检测相关Service是否还保持后台运行
     * @param context
     * @param serviceName "com.文件夹.文件夹.service文件名称"
     * @return
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            //com.example.MyService
            if (serviceName.equals(service.service.getClassName())) {
                Log.i(TAG, service.service.getClassName());
                return true;
            }
        }
        return false;
    }
}
