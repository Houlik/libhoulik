package com.houlik.libhoulik.android.view;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.List;

/**
 * 这是点击左右按钮让照片左右切换
 * 不再使用,改为使用Viewpager
 * Created by Houlik on 20/1/2017.
 */

public class ImageSwitcher implements ViewSwitcher.ViewFactory{

    private Activity activity;
    private android.widget.ImageSwitcher imageSwitcher;
    private int indexPic = 0;
    private List<Integer> list;

    public ImageSwitcher(Activity activity, int isResID, List<Integer> list){
        this.activity = activity;
        init(isResID);
        this.list = list;
    }

    public void init(int isResID){
        imageSwitcher = activity.findViewById(isResID);
        imageSwitcher.setFactory(this);
        imageSwitcher.setImageResource(list.get(indexPic));
    }

    @Override
    public View makeView() {
        return new ImageView(activity);
    }

    //前进
    public void forward(){
        //如果大于数组的长度将不再加
        indexPic++;
        if(indexPic >= list.size()){
            indexPic = list.size()-1;
        }
        imageSwitcher.setImageResource(list.get(indexPic));
    }

    //后退
    public void backForward(){
        //如果小于0将不再减
        indexPic--;
        if(indexPic == -1){
            indexPic = indexPic + 1;
        }
        imageSwitcher.setImageResource((Integer) list.get(indexPic));
    }
}
