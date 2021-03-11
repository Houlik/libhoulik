package com.houlik.libhoulik.android.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

/**
 * Created by houlik on 2018/7/9.
 */

public class RandomUtils {

    private static RandomUtils randomUtils = new RandomUtils();

    private RandomUtils(){}

    public static RandomUtils getInstance(){
        if(randomUtils == null){
            new RandomUtils();
        }
        return randomUtils;
    }

    /**
     * 这是把随机数保存在集合中返回
     * 集合中的数字 不重复以及不排列
     * @param numRandom 数目定位 - 意味随机数只能在指定的数目内
     * @param collectionSize 需要多少个数目保存在集合中
     * @return
     */
    public List randomLinkedHashSet(int numRandom, int collectionSize){
        LinkedHashSet set = new LinkedHashSet();
        List list = new ArrayList();
        Random random = new Random();
        while (set.size() < collectionSize) {
            int num = random.nextInt(numRandom);
            set.add(num);
        }
        list.addAll(set);
        return list;
    }

    public List randomBetween2Num(int numRandom, int collectionSize){
        LinkedHashSet set = new LinkedHashSet();
        List list = new ArrayList();
        Random random = new Random();
        int less = 50;

        while (set.size() < collectionSize) {
            int num = random.nextInt(numRandom) + less;
            if(num >= numRandom - 50){

            }else{
                if(set.size() > 1){
                    while(set.iterator().hasNext()){
                        int tmp = (int) set.iterator().next();
                        if((tmp - num) <= 100){
                            randomLinkedHashSet(numRandom, collectionSize);
                        }else{
                            set.add(num);
                        }
                    }
                }else{
                    set.add(num);
                }

            }


        }
        list.addAll(set);
        return list;
    }
}
