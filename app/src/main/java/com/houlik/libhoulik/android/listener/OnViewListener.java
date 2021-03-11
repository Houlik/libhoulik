package com.houlik.libhoulik.android.listener;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 获取View中画布画笔位图值
 * Created by houlik on 2018/5/6.
 */

public interface OnViewListener {
    void getViewElement(Canvas canvas, Paint paint, Bitmap bitmap);
}
