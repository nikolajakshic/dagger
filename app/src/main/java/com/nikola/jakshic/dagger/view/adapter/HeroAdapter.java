package com.nikola.jakshic.dagger.view.adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.dagger.HeroDiffCallback;
import com.nikola.jakshic.dagger.databinding.ItemHeroBinding;
import com.nikola.jakshic.dagger.inspector.HeroInspector;
import com.nikola.jakshic.dagger.model.Hero;

import java.util.List;

public class HeroAdapter extends DataAdapter<Hero, ItemHeroBinding> {

    public HeroAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindViewHolder(Context context, DataBindHolder<ItemHeroBinding> holder, Hero item) {
        holder.binding.setViewModel(new HeroInspector(context, item));
    }

    @Override
    public ItemHeroBinding createBinding(ViewGroup parent) {
        return ItemHeroBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
    }

    @Override
    public void addData(List<Hero> data) {
        HeroDiffCallback diffCallback = new HeroDiffCallback(list, data);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        list = data;

        diffResult.dispatchUpdatesTo(this);
    }
}