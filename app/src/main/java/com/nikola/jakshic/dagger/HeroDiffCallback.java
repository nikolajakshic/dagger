package com.nikola.jakshic.dagger;

import android.support.v7.util.DiffUtil;

import com.nikola.jakshic.dagger.model.Hero;

import java.util.List;

public class HeroDiffCallback extends DiffUtil.Callback {

    private List<Hero> oldList;
    private List<Hero> newList;

    public HeroDiffCallback(List<Hero> oldList, List<Hero> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getID() == newList.get(newItemPosition).getID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getID() == newList.get(newItemPosition).getID();
    }
}