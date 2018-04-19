package com.nikola.jakshic.dagger.diffcallback;

import android.support.v7.util.DiffUtil;

import com.nikola.jakshic.dagger.model.match.Match;

public class MatchDiffCallback extends DiffUtil.ItemCallback<Match> {
    @Override
    public boolean areItemsTheSame(Match oldItem, Match newItem) {
        return oldItem.getMatchId() == newItem.getMatchId();
    }

    @Override
    public boolean areContentsTheSame(Match oldItem, Match newItem) {
        return oldItem.equals(newItem);
    }
}