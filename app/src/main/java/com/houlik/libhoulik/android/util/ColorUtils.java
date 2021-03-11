package com.houlik.libhoulik.android.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.MotionEvent;

import com.houlik.libhoulik.android.listener.OnPickColorListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 颜色工具
 * Created by Houlik on 2018-04-06.
 */

public class ColorUtils {

    private final String TAG = "ColorUtils : ";
    private static ColorUtils colorUtils = new ColorUtils();
    private final int[] RGB = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private final String[] HEX = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
    private final String[] ALPHA = {"ff", "e6", "d9", "cc", "b3", "99", "80", "66", "4d", "33", "26", "1a", "0d", "00"};
    private final int[] ALPHA_PERCENT = {0, 5, 10, 15, 20, 30, 40, 50, 60, 70, 80, 85, 90, 100};
    private final String[] ALPHA_FULL_HEX = {
            "FF", "FC", "FA", "F7", "F5", "F2", "F0", "ED", "EB", "E8", "E6", "E3", "E0", "DE", "DB", "D9", "D6", "D4", "D1", "CC",
            "C9", "C7", "C4", "C2", "BF", "BD", "BA", "B8", "B5", "B3", "B0", "AD", "AB", "A8", "A6", "A3", "A1", "9E", "9C", "99",
            "96", "94", "91", "8F", "8C", "8A", "87", "85", "82", "80", "7D", "7A", "78", "75", "73", "70", "6E", "6B", "69", "66",
            "63", "61", "5E", "5C", "59", "57", "54", "52", "4F", "4D", "4A", "47", "45", "42", "40", "3D", "3B", "38", "36", "33",
            "30", "2E", "2B", "29", "26", "24", "21", "1E", "1C", "1A", "17", "14", "12", "0F", "0D", "0A", "07", "05", "03", "00"
    };

    private int[] ALPHA_FULL_PERCENT;

    //集合
    private Map<Integer, String> map = new HashMap<>();
    //16进制
    private final int HEX16 = 16;
    //rgb颜色值
    private final int HIGH_PIXEL = 255 + 1;
    //倍数
    private int superpositionOfMultiple = 4;

    //传递颜色值的接口对象
    private OnPickColorListener onPickColorListener;

    private ColorUtils() {
        for (int i = 0; i < RGB.length; i++) {
            //保存RGB和HEX数组到map集合
            map.put(RGB[i], HEX[i]);
        }

        ALPHA_FULL_PERCENT = new int[100];
        for (int i = 0; i < 100; i++) {
            ALPHA_FULL_PERCENT[i] = i;
        }
    }

    public static ColorUtils getInstance() {
        if (colorUtils == null) {
            new ColorUtils();
        }
        return colorUtils;
    }

    /**
     * 单个颜色值转换16进制 / alpha值可以直接在此转换
     *
     * @param num
     * @return 返回双数 无法双拼
     */
    private String calculateHex(int num) {
        //绝对值
        int tmpDivide = Math.abs(num / HEX16);
        //求 num 余数
        int tmpBalance = num % HEX16;
        return map.get(tmpDivide) + map.get(tmpBalance);
    }

    /**
     * 单个颜色值转换16进制 / alpha值可以直接在此转换
     * @param num
     * @return 返回单数 可双拼
     */
    private String calculate2Hex(int num) {
        //绝对值
        int tmpDivide = Math.abs(num / HEX16);
        //求 num 余数
        int tmpBalance = num % HEX16;
        return map.get(tmpBalance);
    }


    /**
     * RGB值转Hex16进制
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    public String pixel2Hex(int r, int g, int b) {
        String red = calculateHex(r);
        String green = calculateHex(g);
        String blue = calculateHex(b);
        return red + green + blue;
    }

    /**
     * 根据自定义设置得到简单的几种颜色
     *
     * @param color
     * @param maxNumber               颜色最高255，for循环设置256
     * @param superpositionOfMultiple 倍数叠加 x8 x16 x32 x64 x128
     * @return
     */
    private int getSimpleColor(int color, int maxNumber, int superpositionOfMultiple) {
        int tmp_Color = 0;
        for (int i = 0; i < maxNumber; i += superpositionOfMultiple) {
            if (color >= i && color < i + superpositionOfMultiple) {
                tmp_Color = i;
            }
        }
        return tmp_Color;
    }

    /**
     * 根据自定义设置,经过判断得到最基本的RGB值转Hex16进制
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    public String transferRGB2Hex_Simple(int r, int g, int b) {
        int tmp_r = getSimpleColor(r, HIGH_PIXEL, superpositionOfMultiple);
        int tmp_g = getSimpleColor(g, HIGH_PIXEL, superpositionOfMultiple);
        int tmp_b = getSimpleColor(b, HIGH_PIXEL, superpositionOfMultiple);
        String red = calculateHex(tmp_r);
        String green = calculateHex(tmp_g);
        String blue = calculateHex(tmp_b);
        return red + green + blue;
    }

    /**
     * 得到计算输入值最接近两数之中哪个RGB值转Hex16进制
     *
     * @param r
     * @param g
     * @param b
     * @param lastRGB
     * @param arrColorR
     * @param arrColorG
     * @param arrColorB
     * @return
     */
    public String getProximalPixel2HexValue(int r, int g, int b, int lastRGB, int[] arrColorR, int[] arrColorG, int[] arrColorB) {
        int tmp_r = getMultiProximalValue(r, lastRGB, arrColorR);
        int tmp_g = getMultiProximalValue(g, lastRGB, arrColorG);
        int tmp_b = getMultiProximalValue(b, lastRGB, arrColorB);
        String red = calculateHex(tmp_r);
        String green = calculateHex(tmp_g);
        String blue = calculateHex(tmp_b);
        return red + green + blue;
    }

    /**
     * 计算输入值最接近两数之中哪个值
     *
     * @param value1    集合中第一个值
     * @param value2    集合中第二个值
     * @param userValue 用户值
     * @return 返回正确值
     */
    private int getProximalValue(int value1, int value2, int userValue) {
        int tmpNum = Math.round((value1 + value2) / 2);
        if (userValue < tmpNum) {
            return value1;
        } else {
            return value2;
        }
    }

    /**
     * 需要的RGB颜色值,通过循环得到输入值全部的不同颜色值,转HEX存入集合
     *
     * @param r String[] r = {"0","64","128","255"};
     * @param g String[] g = {"0","64","128","255"};
     * @param b String[] b = {"0","64","128","255"};
     * @return
     */
    public List<String> listHexColor(String[] r, String[] g, String[] b) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < g.length; j++) {
                for (int k = 0; k < b.length; k++) {
                    list.add(pixel2Hex(Integer.parseInt(r[i]), Integer.parseInt(g[j]), Integer.parseInt(b[k])));
                }
            }
        }
        return list;
    }

    /**
     * 从数组中计算及获取最接近左右的值
     *
     * @param userValue  输入值
     * @param lastDigit  最高值 - 数组最终值
     * @param arrayDigit 数值数组
     * @return
     */
    public int getMultiProximalValue(int userValue, int lastDigit, int... arrayDigit) {
        List<Integer> tmpList = new ArrayList<>();
        //临时数组 增加一个临时数据用于计算
        int[] tmpArrayDigit = new int[arrayDigit.length + 1];
        //先计算是否相同值
        for (int i = 0; i < arrayDigit.length; i++) {
            if (tmpList.isEmpty()) {
                if (userValue == arrayDigit[i]) {
                    tmpList.add(arrayDigit[i]);
                }
            }
            tmpArrayDigit[i] = arrayDigit[i];
        }
        tmpArrayDigit[arrayDigit.length] = lastDigit;

        for (int i = 0; i < arrayDigit.length; i++) {
            if (tmpList.isEmpty()) {
                //如果输入值大于第一个数组数据以及小于第二个数组数据,开始判断接近值
                if (userValue > tmpArrayDigit[i] && userValue < tmpArrayDigit[i + 1]) {
                    int num = getProximalValue(tmpArrayDigit[i], tmpArrayDigit[i + 1], userValue);
                    tmpList.add(num);
                }
            }
        }
        return (int) tmpList.get(0);
    }

    /**
     * 点图获取RGB颜色值
     * 回调提取
     *
     * @param bitmap
     * @param event
     * @return 返回 16 进制颜色
     */
    public String pickRGBColor(Bitmap bitmap, MotionEvent event) {

        int red = Color.red(bitmap.getPixel((int) event.getX(), (int) event.getY()));
        int green = Color.green(bitmap.getPixel((int) event.getX(), (int) event.getY()));
        int blue = Color.blue(bitmap.getPixel((int) event.getX(), (int) event.getY()));
        //转16进制
        String tmpR = calculateHex(red);
        String tmpG = calculateHex(green);
        String tmpB = calculateHex(blue);

        onPickColorListener.getPickColorHexRGB(tmpR + tmpG + tmpB);
        onPickColorListener.getPickColorRGB(red, green, blue);

        return tmpR + tmpG + tmpB;
    }

    /**
     * 按照矩阵点击坐标取得当前颜色
     *
     * @param bitmap
     * @param x
     * @param y
     * @param matrix
     * @return
     */
    public String pickMatrixRGBColor(Bitmap bitmap, float x, float y, Matrix matrix) throws Exception{
        if (x < bitmap.getWidth() & y < bitmap.getHeight() && x >= 0 & y >= 0) {
            //矩阵存放数组
            final float[] values = new float[9];
            //获取矩阵信息
            matrix.getValues(values);
            //得到缩放的倍数
            float zoomMultiples = values[Matrix.MSCALE_X];
            //得到当前XY坐标
            float eventX = x;
            float eventY = y;
            //得到平移的距离
            float translateX = values[Matrix.MTRANS_X];
            float translateY = values[Matrix.MTRANS_Y];
            int red = Color.red(bitmap.getPixel((int) ((eventX - translateX) / zoomMultiples), (int) ((eventY - translateY) / zoomMultiples)));
            int green = Color.green(bitmap.getPixel((int) ((eventX - translateX) / zoomMultiples), (int) ((eventY - translateY) / zoomMultiples)));
            int blue = Color.blue(bitmap.getPixel((int) ((eventX - translateX) / zoomMultiples), (int) ((eventY - translateY) / zoomMultiples)));
            //转16进制
            String tmpR = calculateHex(red);
            String tmpG = calculateHex(green);
            String tmpB = calculateHex(blue);
            return tmpR + tmpG + tmpB;

        }
        return null;
    }


    /**
     * Alpha颜色值转16进制
     *
     * @param a
     * @return
     */
    public String pickAlphaColor(int a) {
        String tmpA = calculateHex(a);
        onPickColorListener.getPickColorHexAlpha(tmpA);
        onPickColorListener.getPickColorAlpha(a);
        return tmpA;
    }

    //把接口的实现,传回给当前类创建的接口对象
    public void setOnPickColorListener(OnPickColorListener onPickColorListener) {
        this.onPickColorListener = onPickColorListener;
    }

    /**
     * 牛逼方法牛逼人使用
     * 转16581375次for循环
     * 牛硬件才能使用
     * <p>
     * APRIL FOOL'S DAY
     * <p>
     * 把RGB三种值全部保存到集合中
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    private List ascendingColorRGB(int r, int g, int b) {
        List<String> listFriggingAwesome = new ArrayList<>();
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < g; j++) {
                for (int k = 0; k < b; k++) {
                    listFriggingAwesome.add(pixel2Hex(i, j, k));
                }
            }
        }
        return listFriggingAwesome;
    }

    /**
     * 随机颜色
     * @return
     */
    public String randomColor(){
        String R1 = null;
        String R2 = null;
        String G1 = null;
        String G2 = null;
        String B1 = null;
        String B2 = null;

        for (int i = 0; i < 15; i++) {
            Random ri = new Random();
            R1 = calculate2Hex(RGB[ri.nextInt(15)]);
            R2 = calculate2Hex(RGB[ri.nextInt(15)]);
            for (int j = 0; j < 7; j++) {
                Random rj = new Random();
                G1 = calculate2Hex(RGB[rj.nextInt(15)]);
                G2 = calculate2Hex(RGB[rj.nextInt(15)]);
                for (int k = 0; k < 3; k++) {
                    Random rk = new Random();
                    B1 = calculate2Hex(RGB[rk.nextInt(15)]);
                    B2 = calculate2Hex(RGB[rk.nextInt(15)]);
                }
            }
        }
        return R1+R2+G1+G2+B1+B2;
    }
}