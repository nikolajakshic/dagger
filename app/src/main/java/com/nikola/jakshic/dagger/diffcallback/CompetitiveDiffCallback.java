package com.nikola.jakshic.dagger.diffcallback;

import android.support.v7.util.DiffUtil;

import com.nikola.jakshic.dagger.model.Competitive;

public class CompetitiveDiffCallback extends DiffUtil.ItemCallback<Competitive> {
    @Override
    public boolean areItemsTheSame(Competitive oldItem, Competitive newItem) {
        return oldItem.getMatchId() == newItem.getMatchId();
    }

    @Override
    public boolean areContentsTheSame(Competitive oldItem, Competitive newItem) {
        return oldItem.equals(newItem);
    }
}