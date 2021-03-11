package com.houlik.libhoulik.android.Register;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

/**
 * 动态注册权限
 * Created by Houlik on 2017/8/20.
 */

public class RegisterPermission {
    private static final int REQUEST_CODE = 1;
    private static final String MOUNT_UNMOUNT_FILESYSTEMS = Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS;
    //android.permission.READ_EXTERNAL_STORAGE 读取外置卡
    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    //android.permission.WRITE_EXTERNAL_STORAGE 写入外置卡
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    //android.permission.WRITE_SETTINGS 写的权限
    private static final String WRITE_SETTINGS = Manifest.permission.WRITE_SETTINGS;
    //android.permission.SEND_SMS 发送短信
    private static final String SEND_SMS = Manifest.permission.SEND_SMS;
    //android.permission.READ_SMS 读取短信
    private static final String READ_SMS = Manifest.permission.READ_SMS;
    //android.permission.RECEIVE_SMS 接收短信
    private static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    //android.permission.READ_PHONE_STATE 读取电话状态
    private static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    //android.permission.INTERNET 网络
    private static final String INTERNET = Manifest.permission.INTERNET;
    private static final String ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;
    private static final String ACCESS_WIFI_STATE = Manifest.permission.ACCESS_WIFI_STATE;
    private static final String CHANGE_NETWORK_STATE = Manifest.permission.CHANGE_NETWORK_STATE;
    private static final String CHANGE_WIFI_STATE = Manifest.permission.CHANGE_WIFI_STATE;

    //android.permission.BATTERY_STATS 电池权限
    private static final String BATTERY_STATS = Manifest.permission.BATTERY_STATS;
    //android.permission.CAMERA 相机权限
    private static final String CAMERA  = Manifest.permission.CAMERA;
    //android.permission.ACCESS_COARSE_LOCATION 定位权限
    private static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    //android.permission.ACCESS_FINE_LOCATION 定位权限
    private static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    //android.permission.RECEIVE_BOOT_COMPLETED 开机权限
    private static final String RECEIVE_BOOT_COMPLETED = Manifest.permission.RECEIVE_BOOT_COMPLETED;
    //振动权限
    private static final String VIBRATE = Manifest.permission.VIBRATE;
    //android.permission.BLUETOOTH 蓝牙
    private static final String BLUETOOTH = Manifest.permission.BLUETOOTH;
    private static final String BLUETOOTH_ADMIN = Manifest.permission.BLUETOOTH_ADMIN;

    private static final String ASEC_CREATE = "android.permission.ASEC_CREATE";
    private static final String ASEC_ACCESS = "android.permission.ASEC_ACCESS";
    private static final String ASEC_DESTROY = "android.permission.ASEC_DESTROY";
    private static final String ASEC_MOUNT_UNMOUNT = "android.permission.ASEC_MOUNT_UNMOUNT";

    /**
     * 6.0 版本以上需要此方法弹出对话框用于用户确认是否开启权限
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity, String[] permissions_Storage) {
        try {
            for (int i = 0; i < permissions_Storage.length ; i++) {
                int permission = ActivityCompat.checkSelfPermission(activity,permissions_Storage[i]);
                if(permission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(activity, permissions_Storage, REQUEST_CODE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加白名单
     * 需要在manifest中声明权限
     * <uses-permission android:name="android.permission.WHITELIST_RESTRICTED_PERMISSIONS" />
     * 以及危险权限都必须在manifest中声明
     * "android.permission.WHITELIST_RESTRICTED_PERMISSIONS"
     * "android.permission.READ_PRIVILEGED_PHONE_STATE"
     *
     * 此注册方法必须在 onRequestPermissionsResult 回调处使用
     * if (requestCode == 1) {
     *             if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
     *                          //do something
     *             }
     * }
     *
     * @param context
     * @param packageName "com.?.?.activity"
     * @param permission RegisterPermission.getSendSms() 如: Manifest.permission.SEND_SMS
     * @param flagWhitelist PackageManager.FLAG_PERMISSION_WHITELIST_SYSTEM | FLAG_PERMISSION_WHITELIST_INSTALLER | FLAG_PERMISSION_WHITELIST_UPGRADE
     */
    public static boolean verifyImportantPermission(Context context, String packageName, String permission, int flagWhitelist){
        PackageManager packageManager = context.getPackageManager();
        boolean ret = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ret = packageManager.addWhitelistedRestrictedPermission(packageName, permission, flagWhitelist);
            return ret;
        }
        return ret;
    }

    public static String getMountUnmountFilesystems() {
        return MOUNT_UNMOUNT_FILESYSTEMS;
    }

    public static String getReadExternalStorage() {
        return READ_EXTERNAL_STORAGE;
    }

    public static String getWriteExternalStorage() {
        return WRITE_EXTERNAL_STORAGE;
    }

    public static String getWriteSettings() {
        return WRITE_SETTINGS;
    }

    public static String getSendSms() {
        return SEND_SMS;
    }

    public static String getReadSms() {
        return READ_SMS;
    }

    public static String getReceiveSms() {
        return RECEIVE_SMS;
    }

    public static String getReadPhoneState() {
        return READ_PHONE_STATE;
    }

    public static String getINTERNET() {
        return INTERNET;
    }

    public static String getAccessNetworkState() {
        return ACCESS_NETWORK_STATE;
    }

    public static String getAccessWifiState() {
        return ACCESS_WIFI_STATE;
    }

    public static String getChangeNetworkState() {
        return CHANGE_NETWORK_STATE;
    }

    public static String getChangeWifiState() {
        return CHANGE_WIFI_STATE;
    }

    public static String getBatteryStats() {
        return BATTERY_STATS;
    }

    public static String getCAMERA() {
        return CAMERA;
    }

    public static String getAccessCoarseLocation() {
        return ACCESS_COARSE_LOCATION;
    }

    public static String getAccessFineLocation() {
        return ACCESS_FINE_LOCATION;
    }

    public static String getReceiveBootCompleted() {
        return RECEIVE_BOOT_COMPLETED;
    }

    public static String getVibrate(){return VIBRATE;}

    public static String getBLUETOOTH() {
        return BLUETOOTH;
    }

    public static String getBluetoothAdmin() {
        return BLUETOOTH_ADMIN;
    }
}
