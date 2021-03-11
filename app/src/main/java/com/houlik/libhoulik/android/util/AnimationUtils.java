package com.houlik.libhoulik.android.util;

import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

/**
 * 帧动画
 * Created by houlik on 2018/5/18.
 */

public class AnimationUtils {

    /**
     * <animation-list xmlns:android="http://schemas.android.com/apk/res/android">
     *      <item
     *          android:drawable="@drawable/0"
     *          android:duration="100" />
     *      <item
     *          android:drawable="@drawable/1"
     *          android:duration="100" />
     *      <item
     *          android:drawable="@drawable/2"
     *          android:duration="100" />
     * </animation-list>
     */

    public void animation(ImageView imageView, int idImageRes){
        imageView.setImageResource(idImageRes);
        AnimationDrawable tmpAnim = (AnimationDrawable) imageView.getDrawable();
        tmpAnim.start();
    }



}
