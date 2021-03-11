package com.houlik.libhoulik.houlik.pixel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.houlik.libhoulik.android.listener.OnBitmapListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 图片转Pixel图
 * 启动子线程计算
 * 注意事项: 如果传人的是集合, 集合中的数量必须是 (宽方块数量x2+1+宽高方块数量)
 * Created by houlik on 2018/5/24.
 */

public class PixelViewThread extends Thread {

    private final String TAG = "Pixel View Thread : ";
    //用于参照的位图
    private Bitmap sampleBitmap;
    //改编的像素位图
    private Bitmap pixelBitmap;
    //方块尺寸
    private int blockSize;
    //方块间距
    private int blockSpacingAdjustment = 0;
    //宽度方块数量
    private int blockWidthQty;
    //高度方块数量
    private int blockHeightQty;
    //提取参照位图的颜色值
    private List listBlockColor = new ArrayList();
    //位图监听器
    private OnBitmapListener onBitmapListener;


    public PixelViewThread(Bitmap sampleBitmap, int blockSize) {
        this.sampleBitmap = sampleBitmap;
        this.blockSize = blockSize;
        this.blockSpacingAdjustment = blockSpacingAdjustment;
        blockWidthQty = sampleBitmap.getWidth() / blockSize;
        blockHeightQty = sampleBitmap.getHeight() / blockSize;
    }

    @Override
    public void run() {
        listBitmapColor();
        drawPixelBitmap();
//        calList();
    }

    /**
     * 获取参照位图的颜色
     */
    private void listBitmapColor() {
        for (int i = 0; i < sampleBitmap.getWidth(); i++) {
            for (int j = 0; j < sampleBitmap.getHeight(); j++) {
                if (i % blockSize == 0 && j % blockSize == 0) {
                    listBlockColor.add(sampleBitmap.getPixel(i, j));
                }
            }
        }
        Log.i(TAG, "颜色集合数量 : " + listBlockColor.size());
    }

    //获取位图的颜色种类与数量 =================未完成
    private void calList() {
        List list = listBlockColor;
        Collections.sort(list);
        Set uniqueSet = new HashSet(list);
        for (Object obj : uniqueSet) {
            System.out.println(obj + " : " + Collections.frequency(list, obj));
        }
    }

    /**
     * 绘制新的位图
     *
     * @return
     */
    private Bitmap drawPixelBitmap() {
        //改编的像素位图
        pixelBitmap = Bitmap.createBitmap(blockSize * blockWidthQty, blockSize * blockHeightQty, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(pixelBitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        int count = 0;
        for (int i = 0; i <= blockWidthQty; i++) {
            for (int j = 0; j <= blockHeightQty; j++) {
                paint.setColor((Integer) listBlockColor.get(count));
                if (i == 0 && j == 0) {
                    canvas.drawRect(new RectF(i * blockSize, j * blockSize, i * blockSize + blockSize, j * blockSize + blockSize), paint);
                } else {
                    canvas.drawRect(new RectF(i * blockSize + blockSpacingAdjustment, j * blockSize + blockSpacingAdjustment, i * blockSize + blockSize, j * blockSize + blockSize), paint);
                }
                count++;
            }
        }
        Log.i(TAG,"画笔数量 : " + count);
        return pixelBitmap;
    }

    /**
     * 完成的位图监听器
     *
     * @param onBitmapListener
     */
    public void setOnBitmapListener(OnBitmapListener onBitmapListener) {
        this.onBitmapListener = onBitmapListener;
        onBitmapListener.getBitmap(pixelBitmap, null);
    }
}
