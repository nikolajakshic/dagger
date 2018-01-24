package com.nikola.jakshic.truesight.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.truesight.databinding.ItemMatchBinding;
import com.nikola.jakshic.truesight.model.Match;
import com.nikola.jakshic.truesight.inspector.MatchInspector;

public class MatchAdapter extends DetailAdapter<Match, ItemMatchBinding> {

    public MatchAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindViewHolder(Context context, DataBindHolder<ItemMatchBinding> holder, Match item) {
        holder.binding.setViewModel(new MatchInspector(context, item));
    }

    @Override
    public ItemMatchBinding createBinding(ViewGroup parent) {
        return ItemMatchBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
    }
}