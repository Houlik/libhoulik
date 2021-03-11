package com.houlik.libhoulik.android.Register;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

/**
 * 动态与静态广播注册
 * Created by Houlik on 2017/10/5.
 */
public class RegisterBroadcastReceiver {

    /**
     * 不能静态注册的广播
     * android.intent.action.SCREEN_ON
     * android.intent.action.SCREEN_OFF
     * android.intent.action.BATTERY_CHANGED
     * android.intent.action.CONFIGURATION_CHANGED
     * android.intent.action.TIME_TICK
     */

    //dynamic permission register
    //Battery
    public static final String BATTERY_CHANGED = "android.intent.action.BATTERY_CHANGED";
    public static final String POWER_CONNECTED = "android.intent.action.ACTION_POWER_CONNECTED";
    public static final String POWER_DISCONNECTED = "android.intent.action.ACTION_POWER_DISCONNECTED";
    public static final String BATTERY_LOW = "android.intent.action.BATTERY_LOW";
    public static final String BATTERY_OKAY = "android.intent.action.BATTERY_OKAY";

    private static RegisterBroadcastReceiver registerBroadcastReceiver = new RegisterBroadcastReceiver();
    private RegisterBroadcastReceiver(){}
    public static RegisterBroadcastReceiver getInstance(){
        if(registerBroadcastReceiver == null){
            new RegisterBroadcastReceiver();
        }
        return registerBroadcastReceiver;
    }

    /**
     * 动态注册广播
     * @param context
     * @param action
     * @param broadcastReceiver
     */
    public void dynamicRegisterBroadcastReceiver(Context context, String action, BroadcastReceiver broadcastReceiver){
        IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(action);
        context.registerReceiver(broadcastReceiver,intentFilter);
    }

    /**
     * 动态注册多个Action的广播
     * @param context
     * @param action
     * @param broadcastReceiver
     */
    public void dynamicRegisterMultiActionBroadcastReceiver(Context context, String[] action, BroadcastReceiver broadcastReceiver){
        IntentFilter intentFilter = new IntentFilter();
        for (int i = 0; i < action.length; i++) {
            intentFilter.addAction(action[i]);
        }
        context.registerReceiver(broadcastReceiver,intentFilter);
    }

    /**
     * 注销动态广播
     * @param context
     * @param broadcastReceiver
     */
    public void unRegisterDynamicBroadcastReceiver(Context context, BroadcastReceiver broadcastReceiver){
        context.unregisterReceiver(broadcastReceiver);
    }

    /**
     * 在不退出软件情况下注册静态广播
     * @param context
     * @param packageName 广播所在的文件夹名称 "com.example.folderName"
     * @param className 广播文件名称 "className.class.getName()"
     */
    public void register(Context context, String packageName, String className){
        context.getPackageManager().setComponentEnabledSetting(new ComponentName(packageName, className),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * 在不退出软件情况下注销静态广播
     * @param context
     * @param packageName 广播所在的文件夹名称 "com.example.folderName"
     * @param className 广播文件名称 "className.class.getName()"
     */
    public void unregister(Context context, String packageName, String className){
        context.getPackageManager().setComponentEnabledSetting( new ComponentName(packageName, className),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
