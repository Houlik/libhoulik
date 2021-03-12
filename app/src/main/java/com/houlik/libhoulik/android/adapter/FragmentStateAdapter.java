package com.houlik.libhoulik.android.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Fragment 适配器
 * 要显示才会加载fragment,用于非常多的界面时候使用
 * Created by Houlik on 01/05/2017.
 */

public abstract class FragmentStateAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;

    public FragmentStateAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
