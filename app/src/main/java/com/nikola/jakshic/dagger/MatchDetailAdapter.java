package com.nikola.jakshic.dagger;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikola.jakshic.dagger.databinding.ItemMatchDetailCollapseBinding;
import com.nikola.jakshic.dagger.inspector.MatchDetailInspector;
import com.nikola.jakshic.dagger.model.Player;
import com.nikola.jakshic.dagger.model.match.Match;
import com.nikola.jakshic.dagger.view.activity.PlayerActivity;

public class MatchDetailAdapter extends RecyclerView.Adapter<MatchDetailAdapter.MatchDetailViewHolder> {

    private Match match;
    private Context context;

    public MatchDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MatchDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemMatchDetailCollapseBinding binding = ItemMatchDetailCollapseBinding
                .inflate(LayoutInflater.from(parent.getContext()),
                        parent,
                        false);
        return new MatchDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MatchDetailViewHolder holder, int position) {
        Player player = match.getPlayers().get(position);
        boolean expanded = player.isExpanded();

        holder.binding.setInspector(new MatchDetailInspector(match, player, context));

        holder.binding.itemMatchDetailExpand.getRoot().setVisibility(expanded ? View.VISIBLE : View.GONE);

        holder.binding.teamDivider.setVisibility(position == 0 || position == 5 ? View.VISIBLE : View.GONE);
        holder.binding.teamDivider.setVisibility(position == 0 || position == 5 ? View.VISIBLE : View.GONE);
        if (position == 0) {
            holder.binding.teamDivider.setText("The Radiant");
            if (match.isRadiantWin())
                holder.binding.teamDivider.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_trophy, 0);
            else
                holder.binding.teamDivider.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (position == 5) {
            holder.binding.teamDivider.setText("The Dire");
            if (!match.isRadiantWin())
                holder.binding.teamDivider.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_trophy, 0);
            else
                holder.binding.teamDivider.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        holder.binding.teamDivider.setVisibility(position == 0 || position == 5 ? View.VISIBLE : View.GONE);

        holder.binding.teamDividerBot.setVisibility(position == 5 ? View.VISIBLE : View.GONE);

        holder.binding.itemMatchDetailCollapse.findViewById(R.id.player_name).setOnClickListener(v -> {
            if (player.getId() == 0)
                return;
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("player-personaname", player.getPersonaName());
            intent.putExtra("player-account-id", player.getId());
            intent.putExtra("player-avatar-full", player.getAvatarUrl());
            context.startActivity(intent);
        });

        holder.binding.itemMatchDetailCollapse.setOnClickListener(v -> {
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