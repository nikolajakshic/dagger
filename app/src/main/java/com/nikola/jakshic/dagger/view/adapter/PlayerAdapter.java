package com.nikola.jakshic.dagger.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.dagger.databinding.ItemPlayerBinding;
import com.nikola.jakshic.dagger.model.Player;
import com.nikola.jakshic.dagger.inspector.PlayerInspector;

public class PlayerAdapter extends DataAdapter<Player, ItemPlayerBinding> {

    public PlayerAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindViewHolder(Context context, DataBindHolder<ItemPlayerBinding> holder, Player item) {
        holder.binding.setViewModel(new PlayerInspector(context, item));
    }

    @Override
    public ItemPlayerBinding createBinding(ViewGroup parent) {
        return ItemPlayerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
    }
}