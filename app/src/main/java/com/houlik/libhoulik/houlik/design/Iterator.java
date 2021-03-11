package com.houlik.libhoulik.houlik.design;

import java.util.List;

/**
 * 迭代器模式
 * 用于详细遍历以及能够逐个操控
 * Created by houlik on 2018/11/16.
 */

public class Iterator implements java.util.Iterator {

    private List list;
    private int nextIndex;
    private int previousIndex;

    public Iterator(List list){
        this.list = list;
        this.nextIndex = 0;
        this.previousIndex = 0;
    }

    @Override
    public boolean hasNext() {
        if(nextIndex < list.size()){
            return true;
        }else{
            nextIndex = 0;
        }
        return false;
    }

    @Override
    public Object next() {
        Object obj = list.get(nextIndex);
        nextIndex++;
        return obj;
    }

    public void remove(int index) {
        list.remove(index);
    }

    //是否还有上一个
    public boolean hasPrevious(){
        if(previousIndex > 0){
            return true;
        }else{
            previousIndex = list.size();
        }
        return false;
    }

    //获取上一个Object
    public Object previous(){
        previousIndex--;
        Object obj = list.get(previousIndex);
        return obj;
    }
}
