package com.nikola.jakshic.truesight.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.truesight.databinding.ItemPlayerBinding;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.viewModel.PlayerViewModel;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.BindingHolder> {

    private Context context;
    private List<Player> list;

    public PlayerAdapter(Context context, List<Player> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPlayerBinding binding = ItemPlayerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.bind(context, list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<Player> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        ItemPlayerBinding binding;

        public BindingHolder(ItemPlayerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Context context, Player player) {
            binding.setViewModel(new PlayerViewModel(context, player));
            binding.executePendingBindings();
        }
    }
}
