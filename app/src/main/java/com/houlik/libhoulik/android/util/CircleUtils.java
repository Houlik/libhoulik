package com.houlik.libhoulik.android.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author houlik
 * @since 2020/5/31
 */
public class CircleUtils {

    /**
     * PI = PI 圆周率
     * r = radius 半径
     * c = 周长
     * a = angle 角度
     */

    private int centerPointX;
    private int centerPointY;

    private int layoutWidth;
    private int layoutHeight;
    private int layoutSize;

    private int itemWidth;
    private int itemHeight;
    private int itemSize;

    private int itemCount;

    //圆周率 3.14159265359
    private double PI = 3.14;

    /**
     *  x = 半径 X cos(角度)
     *  y = 半径 X sin(角度)
     *  xy = 点的坐标
     *
     *  int x = (int) (centerX + (radius / 2) * Math.cos(Math.toRadians(angle))) - itemWidthSize/2;
     *  int y = (int) (centerY + (radius / 2) * Math.sin(Math.toRadians(angle))) - itemWidthSize/2;
     */

    /**
     * 弧度
     * 弧度 = (角度 X PI) / 180
     */
    public double getRadian(double angle){
        return (angle * this.PI) / 180;
    }

    /**
     * 半径
     * 半径 = 就是要设置坐标点的那一圈半径
     * 半径 = 周长 / PI
     */
    public double getRadius(double perimeter){
        return perimeter / this.PI;
    }

    /**
     * 周长
     * 圆的周长也就是一圈的长度
     * 周长 = (高度|宽度)半径 X PI
     */
    public double getPerimeter(int layoutSize){
        return layoutSize * this.PI;
    }

    /**
     * 圆心角
     * 圆心角 = 360度 / 周围坐标点的总数
     */
    public double getCentralAngle(int itemCount){
        return 360 / itemCount;
    }

    /**
     * arcLength = record * 3.14 * (radius/2) / 180;
     * 弧长
     * 弧长 = 圆心角 X 圆周率 X 半径 ／ 180
     */
    public double getArcLength(double centralAngle, double radius){
        return centralAngle * PI * radius / 180;
    }

    /**
     * angle = arcLength * 180 / 3.14 / (radius/2);
     * 角度
     * 角度 = 弧长 X 180 / PI / 半径
     */
    public double getAngle(double arcLength, double radius){
        return arcLength * 180 / this.PI / radius;
    }

    public int getCenterPointX() {
        return centerPointX;
    }

    public void setCenterPointX(int centerPointX) {
        this.centerPointX = centerPointX;
    }

    public int getCenterPointY() {
        return centerPointY;
    }

    public void setCenterPointY(int centerPointY) {
        this.centerPointY = centerPointY;
    }

    public void setLayoutWidth(int layoutWidth) {
        this.layoutWidth = layoutWidth;
    }

    public void setLayoutHeight(int layoutHeight) {
        this.layoutHeight = layoutHeight;
    }

    /**
     * 获取最大一方的尺寸
     * @return
     */
    public int getLayoutSize() {
        if(this.layoutWidth > this.layoutHeight){
            return layoutWidth;
        }
        return layoutHeight;
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public int getItemSize(){
        if(this.itemWidth >= this.itemHeight){
            return itemWidth;
        }
        return itemHeight;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public double getPI(){
        return this.PI;
    }
}
