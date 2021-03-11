package com.houlik.libhoulik.android.util;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

/**
 * 集合工具
 * Created by Houlik on 2017-11-01.
 */

public class CollectionsUtils {

    private static CollectionsUtils collectionsUtils = new CollectionsUtils();

    private CollectionsUtils(){}

    public static CollectionsUtils getInstance(){
        if(collectionsUtils == null){
            new CollectionsUtils();
        }
        return collectionsUtils;
    }

    /**
     * 有序集合
     * @param list
     * @return
     */
    public List softCollection(List list){
        Collections.sort(list);
        return list;
    }

    /**
     * 按汉字拼音升序排列
     * @param list
     * @return
     */
    public List softMandarinCollection(List list){
        Collections.sort(list, Collator.getInstance(java.util.Locale.CHINA));
        return list;
    }

    /**
     * 随机排序
     * @param list
     * @return
     */
    public List softRandomCollection(List list){
        Collections.shuffle(list);
        return list;
    }
}
