package com.houlik.libhoulik.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author houlik
 * @since 2020/5/31
 */
public class RecycleViewAdapter extends RecyclerView.Adapter {

    private Process process;
    private int layoutInflater;

    public RecycleViewAdapter(int layoutInflater, Process process){
        this.layoutInflater = layoutInflater;
        this.process = process;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutInflater, null, false);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        process.processView(holder, position);
    }

    @Override
    public int getItemCount() {
        return process.processList().size();
    }

    public interface Process{
        //用于传递集合数量
        List processList();
        //用于添加元件以及相关操作
        void processView(RecyclerView.ViewHolder holder, int position);
    }
}
