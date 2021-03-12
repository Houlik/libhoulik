package com.houlik.libhoulik.android.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Fragment 和 Tab 的适配器
 * Created by Houlik on 01/05/2017.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    //要显示的fragment集合
    private List<Fragment> fragmentList;
    //tab名称
    private List<String> tabTitleList;

    /**
     * 单一Fragment集合
     * @param fm
     * @param fragmentList
     */
    public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    /**
     * 配合tab使用
     * Fragment 和 在Tab里显示的名称
     * @param fm
     * @param fragmentList
     * @param tabTitleList
     */
    public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> tabTitleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.tabTitleList = tabTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    //全部fragment的数量
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitleList.get(position);
    }


}
