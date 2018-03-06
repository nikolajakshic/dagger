package com.nikola.jakshic.truesight;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikola.jakshic.truesight.databinding.ItemMatchDetailCollapseBinding;
import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.model.match.Match;

public class MatchDetailAdapter extends RecyclerView.Adapter<MatchDetailAdapter.MatchDetailViewHolder> {

    private Match match;
    private Context context;

    public MatchDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MatchDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMatchDetailCollapseBinding binding =  ItemMatchDetailCollapseBinding
                .inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false);

        return new MatchDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MatchDetailViewHolder holder, int position) {
        Player player = match.getPlayers().get(position);
        boolean expanded = player.isExpanded();

        holder.binding.setInspector(new MatchDetailInspector(match, match.getPlayers().get(position), context));

        holder.itemView.findViewById(R.id.expand_match_detail).setVisibility(expanded ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            player.setExpanded(!expanded);
            notifyItemChanged(position);
        });
    }

    public void addData(Match match) {
        this.match = match;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return match == null || match.getPlayers() == null ? 0 : match.getPlayers().size();
    }

    public class MatchDetailViewHolder extends RecyclerView.ViewHolder {

        private ItemMatchDetailCollapseBinding binding;

        public MatchDetailViewHolder(ItemMatchDetailCollapseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}