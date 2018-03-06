package com.nikola.jakshic.truesight.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.truesight.databinding.ItemMatchBinding;
import com.nikola.jakshic.truesight.inspector.MatchInspector;
import com.nikola.jakshic.truesight.model.match.Match;

public class MatchAdapter extends DataAdapter<Match, ItemMatchBinding> {

    private OnMatchClickListener listener;

    public MatchAdapter(Context context, OnMatchClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public void bindViewHolder(Context context, DataBindHolder<ItemMatchBinding> holder, Match item) {
        holder.binding.setViewModel(new MatchInspector(context, item));
        holder.binding.getRoot().setOnClickListener(v -> listener.onClick(item.getMatchId()));
    }

    @Override
    public ItemMatchBinding createBinding(ViewGroup parent) {
        return ItemMatchBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
    }

    public interface OnMatchClickListener{
        void onClick(long id);
    }
}