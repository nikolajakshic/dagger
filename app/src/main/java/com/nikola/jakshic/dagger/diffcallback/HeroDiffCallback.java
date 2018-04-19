package com.nikola.jakshic.dagger.diffcallback;

import android.support.v7.util.DiffUtil;

import com.nikola.jakshic.dagger.model.Hero;

public class HeroDiffCallback extends DiffUtil.ItemCallback<Hero> {

    @Override
    public boolean areItemsTheSame(Hero oldItem, Hero newItem) {
        return oldItem.getHeroId() == newItem.getHeroId();
    }

    @Override
    public boolean areContentsTheSame(Hero oldItem, Hero newItem) {
        return oldItem.equals(newItem);
    }
}