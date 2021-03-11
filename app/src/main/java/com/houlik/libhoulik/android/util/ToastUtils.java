package com.houlik.libhoulik.android.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * @author houlik
 * @since 2020/5/31
 */
public class ToastUtils {


    public ToastUtils(){}

    /**
     * 显示Toast
     *
     * @param msg 提示信息内容
     */
    private void showToast(Context context, Toast toast, int msg) {
        if (null != toast) {
            toast.setText(msg);
        } else {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
