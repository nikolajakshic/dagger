package com.nikola.jakshic.dagger.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.dagger.databinding.ItemSearchHistoryBinding;
import com.nikola.jakshic.dagger.model.SearchHistory;

public class SearchHistoryAdapter extends DataAdapter<SearchHistory, ItemSearchHistoryBinding> {

    private OnQueryClickListener mListener;

    public SearchHistoryAdapter(Context context, OnQueryClickListener listener) {
        super(context);
        mListener = listener;
    }

    @Override
    public void bindViewHolder(Context context, DataBindHolder<ItemSearchHistoryBinding> holder, SearchHistory item) {
        holder.binding.setViewModel(item);
        holder.binding.getRoot().setOnClickListener(v -> mListener.onClick(item.getQuery()));
    }

    @Override
    public ItemSearchHistoryBinding createBinding(ViewGroup parent) {
        return ItemSearchHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
    }

    public interface OnQueryClickListener {
        void onClick(String query);
    }
}