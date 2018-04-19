package com.nikola.jakshic.dagger.view.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.dagger.databinding.ItemPeerBinding;
import com.nikola.jakshic.dagger.inspector.PeerInspector;
import com.nikola.jakshic.dagger.model.Peer;
import com.nikola.jakshic.dagger.view.activity.PlayerActivity;

public class PeerAdapter extends PagedListAdapter<Peer, PeerAdapter.PeerViewHolder> {

    private Context context;

    public PeerAdapter(Context context, @NonNull DiffUtil.ItemCallback<Peer> diffCallback) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public PeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPeerBinding binding = ItemPeerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new PeerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PeerViewHolder holder, int position) {
        Peer item = getItem(position);
        holder.binding.setInspector(new PeerInspector(context, item));
        // TODO move into viewHolder
        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("player-personaname", item.getPersonaname());
            intent.putExtra("player-account-id", item.getPeerId());
            intent.putExtra("player-avatar-full", item.getAvatarfull());
            context.startActivity(intent);
        });
    }

    public class PeerViewHolder extends RecyclerView.ViewHolder {

        private ItemPeerBinding binding;

        public PeerViewHolder(ItemPeerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}