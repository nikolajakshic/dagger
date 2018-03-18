package com.nikola.jakshic.dagger.view.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class DataBindHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public T binding;

    public DataBindHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}