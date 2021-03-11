package com.houlik.libhoulik.android.util;

import android.content.Context;
//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentActivity;
//import androidx.core.app.FragmentTabHost;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTabHost;

import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.List;

/**
 * 已经过时,不再推荐使用。改使用TabLayout 搭配 ViewPager
 * 封装选项/标签组件/标签控件 FragmentTabHost
 * 启动界面必须继承 FragmentActivity 否则将出现 activity 被销毁错误
 * Created by Houlik on 2017-10-15.
 */

public class TabUtils {

    private Context context;
    private FragmentActivity fragmentActivity;
    private int idFrameLayout;
    private int layoutTabComponent;
    private String[] tabTitle;
    private int idTabTitle;
    private List<Integer> list_TabImage;
    private int idTabImage;
    private Fragment[] fragments;
    private FragmentTabHost fragmentTabHost;
    private Class[] fragmentClass;
    private int idDrawableSelector;

    /**
     *
     * @param contexts getApplicationContent()
     * @param fragmentActivity 继承 FragmentActivity
     * @param idFrameLayout FrameLayout 的 id
     * @param layoutTabComponent 元件所处于的 layout 布局
     * @param tabTitle 选项名称
     * @param idTabTitle 元件名称 id
     * @param list_TabImage 选项图片
     * @param idTabImage 元件图片 id
     * @param fragmentTabHosts
     * @param fragmentClass Fragment 界面的数组类
     * @param idDrawableSelector 选项按钮触发背景图变化
     */
    public TabUtils(Context contexts, FragmentActivity fragmentActivity, int idFrameLayout, int layoutTabComponent,
                    String[] tabTitle, int idTabTitle, List<Integer> list_TabImage, int idTabImage, FragmentTabHost fragmentTabHosts, Class[] fragmentClass, int idDrawableSelector){
        this.context = contexts;
        this.fragmentActivity = fragmentActivity;
        this.idFrameLayout = idFrameLayout;
        this.tabTitle = tabTitle;
        this.idTabTitle = idTabTitle;
        this.list_TabImage = list_TabImage;
        this.idTabImage = idTabImage;
        this.fragmentTabHost = fragmentTabHosts;
        this.fragmentClass = fragmentClass;
        this.idDrawableSelector = idDrawableSelector;
        this.layoutTabComponent = layoutTabComponent;

        initFragmentTabHost();
    }

    /**
     * 初始化选项
     */
    private void initFragmentTabHost(){
        fragmentTabHost.setup(context, fragmentActivity.getSupportFragmentManager(), idFrameLayout);
        for (int i = 0; i < tabTitle.length ; i++) {
            TabHost.TabSpec spec = fragmentTabHost.newTabSpec(tabTitle[i]).setIndicator(getView(i));
            fragmentTabHost.addTab(spec, fragmentClass[i],null);

            fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(idDrawableSelector);
        }
    }

    private View getView(int v){
        View view = View.inflate(context, layoutTabComponent, null);
        ImageView tmpImageView = (ImageView) view.findViewById(idTabImage);
        TextView tmpTextView = (TextView) view.findViewById(idTabTitle);
        tmpImageView.setImageResource(list_TabImage.get(v));
        tmpTextView.setText(tabTitle[v]);
        return view;
    }

}
