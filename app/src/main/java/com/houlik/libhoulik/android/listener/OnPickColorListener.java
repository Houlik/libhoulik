package com.houlik.libhoulik.android.listener;

/**
 * //传递点选的Hex和RGB值接口
 * Created by houlik on 2018/5/10.
 */

public interface OnPickColorListener {
    void getPickColorHexRGB(String hex);
    void getPickColorRGB(int r, int g, int b);
    void getPickColorHexAlpha(String hex);
    void getPickColorAlpha(int alpha);
}
