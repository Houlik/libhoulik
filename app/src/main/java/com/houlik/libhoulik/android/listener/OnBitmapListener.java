package com.houlik.libhoulik.android.listener;

import android.graphics.Bitmap;

import java.io.File;

/**
 * 位图及文件监听器
 * Created by houlik on 2018/5/15.
 */

public interface OnBitmapListener {

    void getBitmap(Bitmap bitmap, File file);

}
