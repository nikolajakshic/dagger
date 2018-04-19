package com.nikola.jakshic.dagger.view.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.dagger.databinding.ItemCompetitiveBinding;
import com.nikola.jakshic.dagger.inspector.CompetitiveInspector;
import com.nikola.jakshic.dagger.model.Competitive;
import com.nikola.jakshic.dagger.view.activity.MatchActivity;

public class CompetitiveAdapter extends PagedListAdapter<Competitive, CompetitiveAdapter.CompetitiveViewHolder> {

    private Context context;

    public CompetitiveAdapter(Context context, @NonNull DiffUtil.ItemCallback<Competitive> diffCallback) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public CompetitiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCompetitiveBinding binding = ItemCompetitiveBinding.inflate(
                LayoutInflater.from(
                        parent.getContext()), parent, false);

        return new CompetitiveViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CompetitiveViewHolder holder, int position) {
        Competitive item = getItem(position);
        holder.binding.setInspector(new CompetitiveInspector(context, item));
        // TODO move into viewHolder
        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, MatchActivity.class);
            intent.putExtra("match-id", item.getMatchId());
            context.startActivity(intent);
        });
    }

    public class CompetitiveViewHolder extends RecyclerView.ViewHolder {

        private ItemCompetitiveBinding binding;

        public CompetitiveViewHolder(ItemCompetitiveBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}