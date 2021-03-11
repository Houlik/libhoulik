package com.houlik.libhoulik.houlik.pixel;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.houlik.libhoulik.houlik.pixel.PixelUtilsLayer.Shape;

/**
 * Created by Houlik on 2018-04-02.
 */

public interface OnPixelUtilsListener {

    void onPixel(ImageView imageView, Bitmap bitmap, int density, Shape shape, int resolution, int size);

}
