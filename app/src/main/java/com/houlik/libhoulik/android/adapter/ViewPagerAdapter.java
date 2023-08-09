package com.houlik.libhoulik.android.adapter;

import androidx.viewpager.widget.PagerAdapter;

import android.view.View;
import android.view.ViewGroup;

import com.houlik.libhoulik.android.listener.OnAdapterPositionListener;

import java.util.List;

/**
 * ViewPager适配器 配合 ImageUtils中设置使用
 * 使用viewPager时候必须使用的 setAdapter 方法
 * Created by Houlik on 21/04/2017.
 */
public class ViewPagerAdapter extends PagerAdapter {

    //传递过来的对象集合
    private List<View> list_view;
    private OnAdapterPositionListener listener;

    /**
     * 集合的构造方法
     * @param list_view
     */
    public ViewPagerAdapter(List<View> list_view){
        this.list_view = list_view;
    }

    /**
     * 初始化
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list_view.get(position));
        return list_view.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list_view.get(position));
    }

    /**
     * 激活数量
     * @return 返回集合中的总数
     */
    @Override
    public int getCount() {
        return list_view.size();
    }

    /**
     * 判断视图与对象是否相等
     * @param view
     * @param object
     * @return
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 开始更新
     * @param container
     */
    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    /**
     * 更新完毕
     * @param container
     */
    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if(listener != null) {
            listener.getPosition(position);
        }
    }

    public void setOnAdapterPositionListener(OnAdapterPositionListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
