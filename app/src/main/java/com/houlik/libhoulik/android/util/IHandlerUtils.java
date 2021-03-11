package com.houlik.libhoulik.android.util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by houlik on 2018/5/2.
 * 执行线程中的操作
 */

public interface IHandlerUtils {

    //在主线程运行子线程执行的任务
    void handlerAction(Handler handler, Message message);
    void handlerMessageAction(Message message);
}
