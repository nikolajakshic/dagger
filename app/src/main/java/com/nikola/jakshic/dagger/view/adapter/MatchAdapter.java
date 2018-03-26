package com.nikola.jakshic.dagger.view.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.dagger.databinding.ItemMatchBinding;
import com.nikola.jakshic.dagger.inspector.MatchInspector;
import com.nikola.jakshic.dagger.model.match.Match;

public class MatchAdapter extends PagedListAdapter<Match, MatchAdapter.MatchViewHolder> {

    private OnMatchClickListener listener;
    private Context context;

    public MatchAdapter(Context context, OnMatchClickListener listener, DiffUtil.ItemCallback<Match> callback) {
        super(callback);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMatchBinding binding = ItemMatchBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        MatchViewHolder holder = new MatchViewHolder(binding);
        holder.binding.getRoot().setOnClickListener(v -> listener.onClick(getItem(holder.getAdapterPosition()).getMatchId()));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        holder.binding.setViewModel(new MatchInspector(context, getItem(position)));
    }

    public interface OnMatchClickListener {
        void onClick(long id);
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {

        private ItemMatchBinding binding;

        public MatchViewHolder(ItemMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}