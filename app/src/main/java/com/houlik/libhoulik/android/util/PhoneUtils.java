package com.houlik.libhoulik.android.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
//import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import androidx.annotation.RequiresApi;

/**
 * Created by Houlik on 2018-03-18.
 */

public class PhoneUtils {

    private TelephonyManager telephonyManager;
    private NetworkUtils networkUtils;

    public PhoneUtils(Context context) {
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        networkUtils = new NetworkUtils(context);
    }

    /**
     * 得到 Sim Serial Number
     * @return
     */
    @SuppressLint("MissingPermission")
    public String getSimSerialNumber() {
        return telephonyManager.getSimSerialNumber();
    }

    /**
     * 判断手机上面SD卡是否插好
     * @return
     */
    public boolean externalStorageStateIsMediaMounted() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前网络是否连接
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isNetConnecting(Context context) {
        /**
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        } else {
            return true;
        }**/
        return networkUtils.checkIsConnect2NetworkCapabilities();
    }

    /**
     * 获取android当前可用内存大小
     * @param context
     * @return
     */
    private String getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }

    private String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 返回唯一的用户ID;就是这张卡的编号神马的
     * @return
     */
    @SuppressLint("MissingPermission")
    public String getIMSI() {
        return telephonyManager.getSubscriberId();
    }

    @SuppressLint("MissingPermission")
    public String getPhoneNumber() {
        return telephonyManager.getLine1Number();
    }

}
