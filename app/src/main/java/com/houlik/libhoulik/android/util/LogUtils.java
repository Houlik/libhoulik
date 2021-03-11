package com.houlik.libhoulik.android.util;

/**
 * 以类名为TAG的搜索定位
 * StackTraceElement targetStack = getTargetStack(TAG);
 * targetStack.getFileName()
 * targetStack.getLineNumber()
 * @author houlik
 * @since 2020/5/31
 */
public class LogUtils {
    public static StackTraceElement getTargetStack(String TAG){
        for (StackTraceElement e: Thread.currentThread().getStackTrace()
             ) {
            if(e.getClassName().contains(TAG)){
                return e;
            }
        }
        return null;
    }
}
