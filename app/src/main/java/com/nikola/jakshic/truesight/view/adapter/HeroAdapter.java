package com.nikola.jakshic.truesight.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.truesight.databinding.ItemHeroBinding;
import com.nikola.jakshic.truesight.model.Hero;
import com.nikola.jakshic.truesight.viewModel.HeroViewModel;

public class HeroAdapter extends DetailAdapter<Hero, ItemHeroBinding> {

    public HeroAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindViewHolder(Context context, DataBindHolder<ItemHeroBinding> holder, Hero item) {
        holder.binding.setViewModel(new HeroViewModel(context, item));
    }

    @Override
    public ItemHeroBinding createBinding(ViewGroup parent) {
        return ItemHeroBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
    }
}