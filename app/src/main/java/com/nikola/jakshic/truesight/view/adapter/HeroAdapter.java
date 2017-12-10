package com.nikola.jakshic.truesight.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.truesight.databinding.ItemHeroBinding;
import com.nikola.jakshic.truesight.model.Hero;
import com.nikola.jakshic.truesight.viewModel.HeroViewModel;

import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.BindingHolder> {

    private Context context;
    private List<Hero> list;

    public HeroAdapter(Context context, List<Hero> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHeroBinding binding = ItemHeroBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.bind(context, list.get(position));
    }

    public void setData(List<Hero> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        private ItemHeroBinding binding;

        public BindingHolder(ItemHeroBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Context context, Hero hero) {
            binding.setViewModel(new HeroViewModel(context, hero));
            binding.executePendingBindings();
        }
    }
}
