package com.houlik.libhoulik.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * GridView适配器
 * 利用GridView来显示图片
 * 设置完毕之后必须存在 gridview.setAdapter()中
 * Created by Houlik on 05/04/2017.
 * ReCreated by Houlik on 12/06/2020.
 */

public class GridViewAdapter extends BaseAdapter{

    private Process process;
    private int layoutInflater;

    public GridViewAdapter(int layoutInflater, Process process){
        this.layoutInflater = layoutInflater;
        this.process = process;
    }

    @Override
    public int getCount() {
        return process.processList().size();
    }

    @Override
    public Object getItem(int i) {
        return process.processList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(layoutInflater, null, false);
        process.processView(i, layout, viewGroup);
        return layout;
    }

    public interface Process{
        //用于传递集合数量
        List processList();
        //用于添加元件以及相关操作
        void processView(int position, View view, ViewGroup viewGroup);
    }
}
