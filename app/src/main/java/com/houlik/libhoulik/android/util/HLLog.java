package com.houlik.libhoulik.android.util;

import android.util.Log;

public class HLLog {

    //是否打印日志
    private static boolean isLog = true;

    /**
     * 打印日常日志
     * @param tag
     * @param message
     */
    public static void i(String tag, String message){
        if(isLog) {
            Log.i(tag, " ===>>> " + message);
        }
    }

    /**
     * 打印测试日志
     * @param tag
     * @param message
     */
    public static void d(String tag, String message){
        if(isLog) {
            Log.d(tag, " ===>>> " + message);
        }
    }

    /**
     * 打印错误日志
     * @param tag
     * @param message
     */
    public static void e(String tag, String message){
        if(isLog) {
            Log.e(tag, " ===>>> " + message);
        }
    }
}
