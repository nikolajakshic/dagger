package com.nikola.jakshic.truesight;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikola.jakshic.truesight.model.Player;
import com.nikola.jakshic.truesight.model.match.Match;

public class MatchDetailAdapter extends RecyclerView.Adapter<MatchDetailAdapter.MatchDetailViewHolder> {

    private Match match;

    @Override
    public MatchDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_match_detail_collapse, parent, false);

        return new MatchDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchDetailViewHolder holder, int position) {
        Player player = match.getPlayers().get(position);
        boolean expanded = player.isExpanded();

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

        public MatchDetailViewHolder(View itemView) {
            super(itemView);
        }
    }
}