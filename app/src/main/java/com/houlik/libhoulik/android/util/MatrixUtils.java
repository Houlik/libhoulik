package com.houlik.libhoulik.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.houlik.libhoulik.android.util.AUtils;

/**
 * 图片移动,缩放
 * Created by houlik on 2018/5/7.
 */

public class MatrixUtils {

    private final String TAG = "Matrix Utils : ";

    //状态
    private int mMode = 0;
    //移动状态
    private final int mMODE_DRAG = 1;
    //缩放状态
    private final int mMODE_ZOOM = 2;
    //矩阵
    private Matrix oMatrix;
    //当前矩阵
    private Matrix oCurrentMatrix;
    //开始坐标点
    private PointF oStartPoint;
    private float startDis;
    private float endDis;
    private PointF midPoint;
    //图片
    private ImageView oImageView;

    private float customWidth;

    private float mOldTouchX;
    private float mOldTouchY;
    private float mNewTouchX;
    private float mNewTouchY;

    //是否停止缩放
    private boolean isStop;

    //矩阵记录
    private float[] arrMatrix = new float[9];

    //缩放倍数最大值
    private float maxScale = 6;

    //初始化屏幕宽度
    private float screenWidth;


    public MatrixUtils(Context context, ImageView imageView, Matrix matrix, int blockSize, int blockQty) {
        this.oImageView = imageView;
        oMatrix = matrix;
        oCurrentMatrix = new Matrix();
        oStartPoint = new PointF();
        //初始化屏幕宽度
        screenWidth = AUtils.getInstance().getScreenWidth(context);
        //自定义的图片大小
        this.customWidth = blockSize * blockQty;
    }

    public MatrixUtils(Matrix matrix) {
        oMatrix = matrix;
        oCurrentMatrix = new Matrix();
        oStartPoint = new PointF();
    }

    /**
     * 缩放与平移
     *
     * @param event
     */
    public void matrixScale(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 手指压下屏幕
            case MotionEvent.ACTION_DOWN:
                mMode = mMODE_DRAG;
                // 记录ImageView当前的移动位置
                oCurrentMatrix.set(oImageView.getImageMatrix());
                oStartPoint.set(event.getX(), event.getY());
                break;
            // 手指在屏幕上移动，该事件会不断被触发
            case MotionEvent.ACTION_MOVE:
                // 拖拉图片
                if (mMode == mMODE_DRAG) {
                    float dx = event.getX() - oStartPoint.x; // 得到x轴的移动距离
                    float dy = event.getY() - oStartPoint.y; // 得到y轴的移动距离
                    // 在没有移动之前的位置上进行移动
                    oMatrix.set(oCurrentMatrix);
                    oMatrix.postTranslate(dx, dy);
                }
                // 放大缩小图片
                else if (mMode == mMODE_ZOOM) {
                    endDis = getDistance(event);// 结束距离
                    if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        float scale = endDis / startDis;// 得到缩放倍数
                        //如果是缩小
                        if(scale < 1){
                            //如果自定义图片的宽度乘于缩放倍数的值小于窗口宽度
                            if((customWidth * arrMatrix[0]) < screenWidth){
                                //停止缩放
                                isStop = true;
                            }else{
                                isStop = false;
                            }
                        //如果放大
                        }else if(scale >= 1){
                            //如果自定义图片的宽度乘于缩放倍数的值大于窗口宽度乘于最高倍数的值
                            if((customWidth * arrMatrix[0]) > (screenWidth * maxScale)){
                                isStop = true;
                            }else{
                                isStop = false;
                            }
                        }
                        if(!isStop) {
                            oMatrix.set(oCurrentMatrix);
                            oMatrix.postScale(scale, scale, midPoint.x, midPoint.y);
                        }
                    }
                }
                break;
            // 手指离开屏幕
            case MotionEvent.ACTION_UP:
            // 当触点离开屏幕，但是屏幕上还有触点(手指)
            case MotionEvent.ACTION_POINTER_UP:
                mMode = 0;
                break;
            // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
            case MotionEvent.ACTION_POINTER_DOWN:
                mMode = mMODE_ZOOM;
                //计算两根手指间的距离
                startDis = getDistance(event);
                // 计算两根手指间的中间点,并且两根手指并拢在一起的时候像素大于10
                if (startDis > 10f) { //
                    midPoint = getMid(event);
                    //记录当前ImageView的缩放倍数
                    oCurrentMatrix.set(oImageView.getImageMatrix());
                }
                break;
        }

        oMatrix.getValues(arrMatrix);
        oImageView.setImageMatrix(oMatrix);
    }

    /**
     * 获取两指之间的距离
     * @param event
     * @return
     */
    private float getDistance(MotionEvent event) {
        float x = event.getX(1) - event.getX(0);
        float y = event.getY(1) - event.getY(0);
        float distance = (float) Math.sqrt(x * x + y * y);//两点间的距离
        return distance;
    }

    /**
     * 取两指的中心点坐标
     * @param event
     * @return
     */
    private static PointF getMid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    /**
     * 当前图片的左右上下移动
     * <p>
     * 1. 得到第一次点击的坐标,设置为旧坐标
     * 2。得到拖动时第二次坐标,设置为新坐标
     * 3。开始判断移动方向
     * 4。把新坐标赋值给旧坐标
     *
     * @param event
     */
    public void matrixTranslate(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mOldTouchX = event.getX();
                mOldTouchY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                mNewTouchX = event.getX();
                mNewTouchY = event.getY();

                //1,1 右下
                if (mNewTouchX > mOldTouchX && mNewTouchY > mOldTouchY) {
                    oMatrix.postTranslate(mNewTouchX - mOldTouchX, mNewTouchY - mOldTouchY);
                    //-1,1 左下
                } else if (mNewTouchX < mOldTouchX && mNewTouchY > mOldTouchY) {
                    oMatrix.postTranslate(mNewTouchX - mOldTouchX, mNewTouchY - mOldTouchY);
                    //-1,-1 左上
                } else if (mNewTouchX < mOldTouchX && mNewTouchY < mOldTouchY) {
                    oMatrix.postTranslate(mNewTouchX - mOldTouchX, mNewTouchY - mOldTouchY);
                    //1,-1 右上
                } else if (mNewTouchX > mOldTouchX && mNewTouchY < mOldTouchY) {
                    oMatrix.postTranslate(mNewTouchX - mOldTouchX, mNewTouchY - mOldTouchY);
                }

                mOldTouchX = mNewTouchX;
                mOldTouchY = mNewTouchY;
        }
        oImageView.setImageMatrix(oMatrix);
    }

    /**
     * 原始状态是一倍,2为开始放大
     *
     * @param sx
     * @param sy
     */
    public void matrixScalePerTimes(float sx, float sy) {
        oMatrix.setScale(sx, sy);
        oImageView.setImageMatrix(oMatrix);
    }

    public static Bitmap matrixRotate(Resources res, int idRes, int degrees) {
        Bitmap tmp = BitmapFactory.decodeResource(res, idRes);
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap bmp = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
        return bmp;
    }
}
