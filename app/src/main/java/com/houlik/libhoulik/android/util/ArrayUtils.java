package com.houlik.libhoulik.android.util;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by houlik on 2018/10/30.
 */

public class ArrayUtils<T> {

    /**
     * 移除数组里相同的数据,相同位置相同数据,前方位置被移除后方位置往前挪
     * @param obj
     * @return
     */
    public Object[] removeArraySameItem(Object... obj){
        //LinkedHashSet 按插入排序
        Set<Object> set = new LinkedHashSet<>();
        for (int i = 0; i < obj.length; i++) {
            set.add(obj[i]);
        }
        Object[] tmp = new Object[set.size()];
        Iterator iterator = set.iterator();
        int count = 0;
        while(iterator.hasNext()){
            tmp[count] = iterator.next();
            count++;
        }
        return tmp;
    }
}
