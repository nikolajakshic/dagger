package com.nikola.jakshic.truesight.view.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class DetailAdapter<T, V extends ViewDataBinding> extends RecyclerView.Adapter<DataBindHolder<V>> {

    private Context context;
    List<T> list;

    public DetailAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @Override
    public DataBindHolder<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        V binding = createBinding(parent);
        return new DataBindHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(DataBindHolder<V> holder, int position) {
        bindViewHolder(context, holder, list.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void addData(List<T> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    public abstract void bindViewHolder(Context context, DataBindHolder<V> holder, T item);

    public abstract V createBinding(ViewGroup parent);
}