package com.houlik.libhoulik.android.listener;

import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 自定义相机的回调监听
 * Created by houlik on 2018/5/29.
 */

public interface OnSurfaceListener {
    void getSurface(SurfaceView view, SurfaceHolder holder);
}
