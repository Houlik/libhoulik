package com.houlik.libhoulik.houlik.lucky;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Lucky2Cal {

    /**
     * 按照参数返回对应的整数数组
     * @param num 单数字
     * @return
     */
    private int[] getTheCorrespondingArrays(@NonNull int num){
        int[] arr0 = {0,4};
        int[] arr1 = {1,7};
        int[] arr2 = {2,5,8};
        int[] arr3 = {3,6,9};

        if(num == arr0[0] | num == arr0[1]){
            return arr0;
        }else if(num == arr1[0] | num == arr1[1]){
            return arr1;
        }else if(num == arr2[0] | num == arr2[1] | num ==arr2[2]){
            return arr2;
        }else if(num == arr3[0] | num == arr3[1] | num == arr3[2]){
            return arr3;
        }else{
            return null;
        }
    }

    /**
     * 将四位数的参数,逐个按照对应的列表,循环后返回通过集合确定无相同四位数的列表
     * 例子: 1234 = 7234 | 1534 | 1834 | 1264 | 1294 | 1230 等...类似四位数
     * @param fourDigitNum 四位数 new String[]{"?","?","?","?"} | new String[]{"1","2","3","4"}
     * @return
     */
    public List<String> getArraysAfterLoop(@NonNull String[] fourDigitNum){
        List<String> list4DigitNum = new ArrayList<>();
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {//四位数的第一个数字
                //按照单数字获取对应的列表
                int[] tmpInt = getTheCorrespondingArrays(Integer.parseInt(fourDigitNum[i]));
                list.add(tmpInt);
            } else if (i == 1) {//四位数的第二个数字
                //按照单数字获取对应的列表
                int[] tmpInt = getTheCorrespondingArrays(Integer.parseInt(fourDigitNum[i]));
                list.add(tmpInt);
            } else if (i == 2) {//四位数的第三个数字
                //按照单数字获取对应的列表
                int[] tmpInt = getTheCorrespondingArrays(Integer.parseInt(fourDigitNum[i]));
                list.add(tmpInt);
            } else if (i == 3) {//四位数的第四个数字
                //按照单数字获取对应的列表
                int[] tmpInt = getTheCorrespondingArrays(Integer.parseInt(fourDigitNum[i]));
                list.add(tmpInt);
            }

        }
        //四次循环代表四位数的单个数字
        for (int i = 0; i < list.get(0).length; i++) {//按照获取到的对应列表来决定循环次数
            for (int j = 0; j < list.get(1).length; j++) {//按照获取到的对应列表来决定循环次数
                for (int k = 0; k < list.get(2).length; k++) {//按照获取到的对应列表来决定循环次数
                    for (int l = 0; l < list.get(3).length; l++) {//按照获取到的对应列表来决定循环次数
                        //按照获取到的每一个列表中的元素,逐个列表循环,获取全部四位数不同数字
                        list4DigitNum.add(String.valueOf(list.get(0)[i]) + list.get(1)[j] + list.get(2)[k] + list.get(3)[l]);
                    }
                }
            }
        }
        return list4DigitNum;
    }
}
