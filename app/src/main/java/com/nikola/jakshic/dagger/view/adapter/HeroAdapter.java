package com.nikola.jakshic.dagger.view.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.dagger.databinding.ItemHeroBinding;
import com.nikola.jakshic.dagger.inspector.HeroInspector;
import com.nikola.jakshic.dagger.model.Hero;

public class HeroAdapter extends PagedListAdapter<Hero, HeroAdapter.HeroViewHolder> {

    private Context context;

    public HeroAdapter(Context context, @NonNull DiffUtil.ItemCallback<Hero> diffCallback) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHeroBinding binding = ItemHeroBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new HeroViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        holder.binding.setViewModel(new HeroInspector(context, getItem(position)));
    }

    public class HeroViewHolder extends RecyclerView.ViewHolder {

        private ItemHeroBinding binding;

        public HeroViewHolder(ItemHeroBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}