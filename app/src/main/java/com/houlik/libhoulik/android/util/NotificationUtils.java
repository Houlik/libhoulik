package com.houlik.libhoulik.android.util;

import android.app.Service;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
//import android.support.annotation.RequiresApi;
import androidx.annotation.RequiresApi;
import android.util.Log;



/**
 * 通知工具
 * Created by Houlik on 2017/9/28.
 */

public class NotificationUtils {

    private final String TAG = "HOULIK NOTIFICATION :";
    private static NotificationUtils notificationUtils = new NotificationUtils();

    private NotificationUtils(){}

    public static NotificationUtils getInstance(){
        if(notificationUtils == null){
            new NotificationUtils();
        }
        return notificationUtils;
    }

    /**
     * 前台 service Foreground Process
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setForegoundNotification(Context context, int resourceID, String title, Service service){
        Log.i(TAG, "启动 Foreground NotificationUtils");
        android.app.Notification.Builder builder = new android.app.Notification.Builder(context);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), resourceID));
        builder.setContentTitle(title);

        android.app.Notification notification = builder.build();
        //NotificationUtils.FLAG_AUTO_CANCEL 当用户点击将被取消
        notification.flags = android.app.Notification.FLAG_AUTO_CANCEL;
        service.startForeground(1, notification);
        Log.i(TAG, "完成 Foreground NotificationUtils");
    }
}
