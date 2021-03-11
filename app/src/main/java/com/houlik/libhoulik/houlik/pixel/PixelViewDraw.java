package com.houlik.libhoulik.houlik.pixel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by houlik on 2018/5/25.
 */

public class PixelViewDraw extends PixelView {

    private Context context;

    public PixelViewDraw(Context context, int blockSize, int blockWidthQty, int blockHeightQty, Shape shape) {
        super(blockSize, blockWidthQty, blockHeightQty, shape);
        this.context = context;
    }

    public PixelViewDraw(Context context, Bitmap bmp, int blockSize) {
        super(bmp, blockSize);
        this.context = context;
    }

    /**
     * 绘画格子前设置画笔颜色与形状
     *
     * @param event
     * @param shape
     * @param color
     * @param style
     */
    private void draw(MotionEvent event, int color, Paint.Style style, PixelView.Shape shape, PixelView.Square square, Matrix matrix) {
        //设置画笔为白色,先把点击坐标清除颜色
        super.setPaintColor(color);
        //设置画笔彩色形状为填充
        super.setPaintStyle(style);
        super.drawSingleShape(event, 0, 0, super.canvas, super.paint, matrix, shape, square);
    }

    //设置画笔为白色,先把点击坐标清除颜色,设置画笔彩色形状为填充,填充点击坐标为白色
    private void setClearBackground(MotionEvent event, Shape shape, Square square, Matrix matrix) {
        draw(event, Color.WHITE, Paint.Style.FILL, shape, square, matrix);
    }

    //设置画笔为当前颜色板所选颜色,设置画笔彩色形状为填充,填充点击坐标为当前颜色板所选颜色
    private void drawBlock(MotionEvent event, int paintColor, Shape shape, Square square, Matrix matrix) {
        draw(event, paintColor, Paint.Style.FILL, shape, square, matrix);
    }

    //设置画笔颜色,设置画笔彩色形状为格子,在点击坐标绘画格子线条
    private void drawBlockLine(MotionEvent event, int paintColor, Shape shape, Square square, Matrix matrix) {
        draw(event, paintColor, Paint.Style.STROKE, shape, square, matrix);
    }

    public void paintingBlock(ImageView showImage, MotionEvent event, Shape shape, int paintColor, Square square, Matrix matrix, boolean isCustomeBitmap) {
        if (isCustomeBitmap) {
            if (shape.equals(PixelView.Shape.DEFAULT)) {
                Toast.makeText(context, "请先选择一种绘制形状", Toast.LENGTH_SHORT).show();
            } else {
                draw(event, paintColor, Paint.Style.FILL, shape, square, matrix);
                //刷新图片
                showImage.invalidate();
            }
        } else {
            if (shape.equals(PixelView.Shape.DEFAULT)) {
                Toast.makeText(context, "请先选择一种绘制形状", Toast.LENGTH_SHORT).show();
            } else {
                setClearBackground(event, shape, square, matrix);
                drawBlock(event, paintColor, shape, square, matrix);
                //刷新图片
                showImage.invalidate();
            }
        }
    }

    /**
     * 绘制底层
     *
     * @param event
     * @param posX
     * @param posY
     * @param matrix
     * @param shape
     * @param color
     * @param style
     */
    private void drawUnderPainting(MotionEvent event, float posX, float posY, Matrix matrix, int color, Paint.Style style, Shape shape, Square square) {
        setPaintColor(color);
        setPaintStyle(style);
        drawSingleShape(event, posX, posY, super.canvas, super.paint, matrix, shape, square);
    }

    /**
     * 橡皮擦
     *
     * @param event
     * @param posX
     * @param posY
     * @param matrix
     * @param shape
     */
    public void eraserTool(MotionEvent event, float posX, float posY, Matrix matrix, Shape shape, Square square) {
        if (shape.equals(Shape.CIRCLE)) {
            drawUnderPainting(event, posX, posY, matrix, Color.WHITE, Paint.Style.FILL, shape, square);
            drawUnderPainting(event, posX, posY, matrix, super.baseLineColor, Paint.Style.STROKE, shape, square);
        } else {
            drawUnderPainting(event, posX, posY, matrix, Color.WHITE, Paint.Style.FILL, shape, square);
        }
    }
}
