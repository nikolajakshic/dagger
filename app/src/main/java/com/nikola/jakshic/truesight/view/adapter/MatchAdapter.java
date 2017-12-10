package com.nikola.jakshic.truesight.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.truesight.databinding.ItemMatchBinding;
import com.nikola.jakshic.truesight.model.Match;
import com.nikola.jakshic.truesight.viewModel.MatchViewModel;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.BindingHolder> {

    private List<Match> list;
    private Context context;

    public MatchAdapter(Context context, List<Match> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMatchBinding binding = ItemMatchBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Match> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {

        private ItemMatchBinding binding;

        public BindingHolder(ItemMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Match match) {
            binding.setViewModel(new MatchViewModel(context, match));
            binding.executePendingBindings();
        }


    }
}
