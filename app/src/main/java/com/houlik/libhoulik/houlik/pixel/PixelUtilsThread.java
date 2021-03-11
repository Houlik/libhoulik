package com.houlik.libhoulik.houlik.pixel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
import android.util.Log;

import com.houlik.libhoulik.houlik.pixel.PixelUtilsLayer.Shape;

/**
 * Created by Houlik on 2018-04-02.
 */

public class PixelUtilsThread extends Thread {

    private final String TAG = "PixelUtilsThread";

    //画笔
    private Paint paint;
    //保持执行
    private boolean isRendering = false;

    //传递需要值
    private OnPixelUtilsListener onPixelUtilsListener;
    private Bitmap bitmap;

    //位图宽度
    private int bmpWidth;
    //位图高度
    private int bmpHeight;
    //列 - Density
    private double cols;
    //自定义的坐标X
    private int targetX = -1;
    //自定义的坐标Y
    private int targetY = -1;
    //自定义宽度
    private double targetWidth;
    //自定义高度
    private double targetHeight;

    //形状
    private Shape shape;
    //分辨率
    private int resolution;
    //大小
    private int size;

    public PixelUtilsThread() {
        Log.i(TAG, "PixelUtilsThread构造方法");
        this.paint = new Paint();
    }

    //设置需要值的监听器
    public void setPixelateListener(OnPixelUtilsListener listener) {
        Log.i(TAG, "监听器");
        this.onPixelUtilsListener = listener;
    }

    //设置位图
    public void setBitmap(@NonNull Bitmap bitmap) {
        Log.i(TAG, "设置位图");
        this.bitmap = bitmap;
        // 获取位图宽高
        this.bmpWidth = this.bitmap.getWidth();
        this.bmpHeight = this.bitmap.getHeight();
        Log.i(TAG, "位图宽 : bmpWidth " + bmpWidth);
        Log.i(TAG, "位图高 : bmpHeight " + bmpHeight);
    }

    //是否保持执行
    boolean isRendering() {
        Log.i(TAG, "保持执行 : " + isRendering);
        return isRendering;
    }

    @Override
    public synchronized void start() {
        Log.i(TAG, "子线程同步开始");
        super.start();
    }

    //设置区域
    public void setArea(int x, int y, int width, int height) {
        targetX = x;
        targetY = y;
        targetWidth = width;
        targetHeight = height;
        Log.i(TAG, "设置区域 targetX : " + x);
        Log.i(TAG, "设置区域 targetY : " + y);
        Log.i(TAG, "设置区域 targetWidth : " + width);
        Log.i(TAG, "设置区域X targetHeight : " + height);
    }

    //密度
    public void pixelate(int density) {
        Log.i(TAG, "密度 pixelate 传递的density: " + density);
        this.cols = density > 1 ? density : 1;
        Log.i(TAG, "密度 pixelate : this.cols = density > 1 ? density : 1 " + this.cols);
    }

    public void getShape(Shape shape) {
        this.shape = shape;
        Log.i(TAG, "得到形状输入 : " + this.shape);
    }

    public void getResolution(int resolution) {
        this.resolution = resolution;
        Log.i(TAG, "得到分辨率输入 : " + this.resolution);
    }

    public void getSize(int size) {
        this.size = size;
        Log.i(TAG, "得到大小输入 : " + this.size);
    }

    @Override
    public void run() {
        Log.i(TAG, "线程运行");
        if (!isRendering()) {
            Log.i(TAG, "设置保持执行");
            isRendering = true;
            Log.i(TAG, "isRendering : " + isRendering);
            Log.i(TAG, "创立画布");
            Canvas canvas = new Canvas(this.bitmap);
            Log.i(TAG, "绘制画布");
            draw(canvas);
        }
    }

    private void draw(Canvas canvas) {
        Log.i(TAG, "绘画中......");
        double startX = 0;
        double startY = 0;
        double blockSize;
        double rows;

        //16进制集合
//        final List<String> colorInt2Hex = new ArrayList();

        // Draw certain area 提取自定义的XY区域
        if (targetX > 0 && targetY > 0) {
            //目标宽度 除于 列 得到像素方块大小
            blockSize = targetWidth / cols;
            Log.i(TAG, "绘画中 : blockSize : " + blockSize + " = targetWidth : " + targetWidth + " / cols : " + cols);
            //目标高度 除于 像素方块大小 得到 行 数
            rows = Math.ceil(targetHeight / blockSize);
            Log.i(TAG, "绘画中 : rows : " + rows + " = Math.ceil(targetHeight " + targetHeight + " / blockSize " + blockSize + ")");
            // Find the column and row by coordinates of an equal sized rectangle
            //目标坐标X 除于 像素方块大小 得到 列 的开始
            double startCol = Math.floor(targetX / blockSize);
            Log.i(TAG, "绘画中 : startCol : " + startCol + " = Math.floor(targetX " + targetX + " / blockSize " + blockSize + ")");
            //目标坐标Y 除于 像素方块大小 得到 行 的开始
            double startRow = Math.floor(targetY / blockSize);
            Log.i(TAG, "绘画中 : startRow : " + startRow + " = Math.floor(targetY " + targetY + " / blockSize " + blockSize + ")");

            // Adjust the coordinates to the grid 调整网格
            startY = (int) startRow * blockSize - (targetWidth / 2);
            Log.i(TAG, "绘画中 : startY : " + startY + " = (int)startRow " + (int) startRow + " * blockSize " + blockSize + " - (targetWidth " + targetWidth + " / 2)");
            startX = (int) startCol * blockSize - (targetWidth / 2);
            Log.i(TAG, "绘画中 : startX : " + startX + " = (int)startCol " + (int) startCol + " * blockSize " + blockSize + " - (targetWidth " + targetWidth + " / 2)");
        } else {
            //位图宽度 除于 列 得到 像素方块大小
            blockSize = bmpWidth / cols;
            Log.i(TAG, "绘画中 : blockSize : "+blockSize+" = bmpWidth "+bmpWidth+" / cols "+cols);

//            targetHeight = blockSize;
//            Log.i(TAG, "绘画中 : targetHeight :"+ targetHeight +" = blockSize "+blockSize);
            //位图高度 除于 像素方块大小 得到 行
            rows = Math.ceil((double) bmpHeight / blockSize);
            Log.i(TAG, "绘画中 : rows :"+rows+" = Math.ceil((double) bmpHeight "+(double)bmpHeight +" / blockSize "+ blockSize +")");
        }

        //循环画布绘制
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                //像素方块大小 乘于 行 加开始坐标Y 得到 像素坐标Y
                double pixelCoordY = blockSize * row + startY;
                //像素方块大小 乘于 列 加开始坐标X 得到 像素坐标X
                double pixelCoordX = blockSize * col + startX;
//                double midY = pixelCoordY + (blockSize / 2);
//                double midX = pixelCoordX + (blockSize / 2);
                //像素坐标Y 加 像素方块大小除于2 得到 像素坐标Y的中间点
                double midY = pixelCoordY + (blockSize / 2);
                //像素坐标X 加 像素方块大小除于2 得到 像素坐标X的中间点
                double midX = pixelCoordX + (blockSize / 2);
                //如果像素坐标X 大于等于 位图宽度 或者 小于 零, 就继续
                if (midX >= bmpWidth || midX < 0) continue;
                //如果像素坐标Y 大于等于 位图高度 或者 小于 零, 就继续
                if (midY >= bmpHeight || midY < 0) continue;

                paint.setColor(bitmap.getPixel((int)midX, (int)midY));

                //得到像素XY坐标中间点用于指定每个像素方块中的颜色
//                paint.setColor(Color.parseColor(setPaintColor(bitmap.getPixel((int) midX, (int) midY))));

                //得到 rgb 转 hex 16进制 的集合，用于得知颜色种类
//                setColorHex2List(colorInt2Hex, paint.getColor());

                canvas.drawRect((float) pixelCoordX, (float) pixelCoordY,
                        (float) (pixelCoordX + blockSize), (float) (pixelCoordY + blockSize), paint);
            }
        }

        isRendering = false;

        //执行线程
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                onPixelUtilsListener.onPixel(null, bitmap, (int) cols, shape, resolution, size);
            }
        });
    }

//    /**
//     * 获取相关图片颜色转化为16进制保存于集合
//     *
//     * @param list
//     * @param color
//     */
//    private void setColorHex2List(List<String> list, int color) {
//        int r = Color.red(color);
//        int g = Color.green(color);
//        int b = Color.blue(color);
//        String hex = ColorUtils.getInstance().getProximalPixel2HexValue(r, g, b);
//        list.add("#" + hex);
//    }

//    private String setPaintColor(int color) {
//        int r = Color.red(color);
//        int g = Color.green(color);
//        int b = Color.blue(color);
//        String hex = ColorUtils.getInstance().getProximalPixel2HexValue(r, g, b);
//        return "#" + hex;
//    }
}
