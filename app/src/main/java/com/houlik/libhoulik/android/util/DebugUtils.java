package com.houlik.libhoulik.android.util;

import android.os.StrictMode;

public class DebugUtils {

    private static DebugUtils debugUtils;

    private DebugUtils(){}

    public static DebugUtils getInstance(){
        if(debugUtils == null){
            debugUtils = new DebugUtils();
        }
        return debugUtils;
    }

    /**
     * 这是用于检测数据库是否没有关闭正确导致内存泄漏, 如果是将自动保存日志并且退出
     * 这方法需要设置在 activity 中的 super.onCreate(savedInstanceState) 之前
     */
    public void checkSQLDatabase(){
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
    }
}
