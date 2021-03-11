package com.houlik.libhoulik.houlik.utils;

import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Houlik 计算工具类
 * Created by Houlik on 02/04/2017.
 */
public class HLUtils {

    private static HLUtils hlUtils = new HLUtils();

    private HLUtils() {}

    public static HLUtils getInstance(){
        if(hlUtils == null){
            new HLUtils();
        }
        return hlUtils;
    }

    /**
     * 拼接所有数组中每一位数目的个位数，拼接后返回整数
     *
     * @param num
     * @return 得到所有个位数的拼接
     */
    public int jointUnitsDigit(int... num){
        //临时数字数组
        int[] tempNum = new int[num.length];
        for(int sn = 0; sn < num.length; sn++) {
            //保存得到的数字到临时数字数组中
            tempNum[sn] = num[sn];
        }
        //临时字符串数组
        String[] tempLocalStr = new String[tempNum.length];
        for(int st = 0; st < tempNum.length ; st++ ){
            //如果数字号码小于等于九
            if(tempNum[st] <= 9){
                //就直接将数字存入字符串数组中
                tempLocalStr[st] = tempNum[st]+"";
                //否则
            } else {
                //把数字转成字符串,赋值给 tempForStr
                String tempForStr = tempNum[st]+"";
                //把字符串最后一位个位数赋值给临时字符变量
                char tempForChar= tempForStr.charAt(tempForStr.length()-1);
                //再把临时字符变量值赋予内部临时字符串变量数组
                tempLocalStr[st] = tempForChar+"";
            }
        }
        //临时字符串拼接变量
        String tempStrJoint = "";
        for (int tsj = 0; tsj < num.length; tsj++) {
            tempStrJoint += tempLocalStr[tsj];
        }
        //返回字符串,使用Integer.parseInt()来转成数字
        return Integer.parseInt(tempStrJoint);
    }

    /**
     * 叠加法
     * 用于计算一个数字在不超越规定值不断重复叠加,每一次叠加后的总数保存到集合中,可以按照集合中的数字查询相关数据
     *
     * 如果用户值大于等于计算值进入判断:
     * 用户值减去计算值先保存集合中, 当用户值小于计算值时开始在for循环中与计算值叠加保存集合中到最终值为止
     * 如果开始值小于计算值, 先把用户值保存集合中再开始执行叠加的计算到最终值为止
     *
     * 如果用户值等于0, 直接计算值叠加保存集合中到最终值为止
     *
     * @param calculatedValue 计算值
     * @param finalValue 最终值, 所计算的数字不能超越此数字
     * @param userValue 输入值, 得到欲计算的数字
     * @return 返回集合类型
     */
    public ArrayList<Integer> addition(final int calculatedValue, final int finalValue, int userValue){
        ArrayList<Integer> list = new ArrayList<>();
        //如果 userValue 大于 calculatedValue
        if(userValue > calculatedValue){
            //当 userValue 大于 calculatedValue 的时候
            while(userValue > calculatedValue) {
                //userValue 等于 userValue 减去 calculatedValue 直到 少于等于 calculatedValue 为止
                userValue = userValue - calculatedValue;
            }
            //把结果存入ArrayList 中
            list.add(userValue);

            //当小于等于 calculatedValue 时候,开始为 userValue 叠加 calculatedValue 到 finalValue 为止
            for(int i = 0; userValue <= finalValue ; i++){
                userValue = userValue + calculatedValue;
                //如果当前的 userValue 小于等于 finalValue,就存入到集合中
                if(userValue <= finalValue) {
                    list.add(userValue);
                }
            }
        //否则
        } else {
            //如果userValue值为零
            if(userValue == 0){
                //直接为 userValue 叠加 calculatedValue 到 finalValue 为止
                for (int i = 0; userValue <= finalValue; i++) {
                    userValue = userValue + calculatedValue;
                    //如果当前的 userValue 小于等于 finalValue,就存入到集合中
                    if (userValue <= finalValue) {
                        list.add(userValue);
                    }
                }
            }else {
                //userValue 小于等于 calculatedValue,就直接存入集合中
                list.add(userValue);
                //直接为 userValue 叠加 calculatedValue 到 finalValue 为止
                for (int i = 0; userValue <= finalValue; i++) {
                    userValue = userValue + calculatedValue;
                    //如果当前的 userValue 小于等于 finalValue,就存入到集合中
                    if (userValue <= finalValue) {
                        list.add(userValue);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 公式计算点击X的具体坐标,以及缩放与移动后点击转换成具体的坐标
     * 主要用于缩放移动后准确计算点击后matrix的坐标
     * @param posX x坐标
     * @param matrix
     * @param regionSize 坐标周围区域大小
     * @return
     */
    private float calCoordinateX(float posX, Matrix matrix, int regionSize){
        //矩阵存放数组
        final float[] values = new float[9];
        //获取矩阵信息
        matrix.getValues(values);
        //得到缩放的倍数
        float zoomMultiples = values[Matrix.MSCALE_X];
        //得到平移的距离
        float translateX = values[Matrix.MTRANS_X];
        // (先把当前各个坐标减去平移距离再除以缩放倍数) 后再减去 (当前坐标减去平移距离再除以缩放倍数) 取 方块尺寸余数
        float coordinateX = (((posX - translateX) / zoomMultiples) - ((posX - translateX) / zoomMultiples) % regionSize);
        return coordinateX;
    }

    /**
     * 公式计算点击Y的具体坐标,以及缩放与移动后点击转换成具体的坐标
     * 主要用于缩放移动后准确计算点击后matrix的坐标
     * @param posY y坐标
     * @param matrix
     * @param regionSize 坐标周围区域大小
     * @return
     */
    private float calCoordinateY(float posY, Matrix matrix, int regionSize){
        //矩阵存放数组
        final float[] values = new float[9];
        //获取矩阵信息
        matrix.getValues(values);
        //得到缩放的倍数
        float zoomMultiples = values[Matrix.MSCALE_Y];
        //得到平移的距离
        float translateY = values[Matrix.MTRANS_Y];
        // (先把当前各个坐标减去平移距离再除以缩放倍数) 后再减去 (当前坐标减去平移距离再除以缩放倍数) 取 方块尺寸余数
        float coordinateY = (((posY - translateY) / zoomMultiples) - ((posY - translateY) / zoomMultiples) % regionSize);
        return coordinateY;
    }

    // 1|0
    // 8192|4096|2048|1024|512|256|128|64|32|16|8|4|2|1

    /**
     * 十进制转二进制
     * @param num
     * @return
     */
    private String decimal2Binary(int num){
        //用于拼接整数
        StringBuilder tmp_binary = new StringBuilder();
        //用于循环于除之用
        int tmp_num = num;
        do {
            //加入剩下的余数
            tmp_binary.append(tmp_num % 2);
            //得到除后剩下的数字
            tmp_num /= 2;
            //如果剩下的数字不是零将继续保持整除
        } while(tmp_num != 0);
        //八位数补两个零
        tmp_binary.append("00");
        //把得到的字符串倒转过来就是二进制
        tmp_binary.reverse();
        //返回拼接倒转后的字符串
        return tmp_binary.toString();
    }

    /**
     * 按照LinkedList状态, 依照固定刷新数量往前添加数据
     * 逐步添加数据的集合将按照指定添加的数量往里添加数据, 直到与完整数据的集合相同为止
     * 如果数据添加已经相同为止, 将返回true, 否则返回false
     * 两边集合的类型以及大小必须相同
     * @param totalDataList 完整数据的数组集合
     * @param refreshList 逐步添加数据的数组集合
     * @param currentRefreshSize 逐步添加数据数组集合的最新已有大小
     * @param count 指定添加的数量
     * @return false 还有数据 true 没有数据
     */
    public boolean addDataFowardAtArrayLinkedLists(List totalDataList, LinkedList[] refreshList, int currentRefreshSize, int count){
        for (int i = (totalDataList.size()-currentRefreshSize); i > 0; i--) {
            if(i > (totalDataList.size() - currentRefreshSize - count) ){
                for (int j = 0; j < totalDataList.size(); j++) {
                    refreshList[j].addLast(totalDataList.get(i-1));
                }
            }
        }
        System.out.println("TotalSize : " + totalDataList.size());
        System.out.println("CurrentSize : " + currentRefreshSize);
        if(totalDataList.size() == currentRefreshSize + 1){
            return true;
        }
        return false;
    }

}
