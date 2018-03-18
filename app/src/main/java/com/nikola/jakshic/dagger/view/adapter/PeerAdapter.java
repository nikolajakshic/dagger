package com.nikola.jakshic.dagger.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nikola.jakshic.dagger.databinding.ItemPeerBinding;
import com.nikola.jakshic.dagger.inspector.PeerInspector;
import com.nikola.jakshic.dagger.model.Peer;
import com.nikola.jakshic.dagger.view.activity.PlayerActivity;

import java.util.List;

public class PeerAdapter extends DataAdapter<Peer, ItemPeerBinding> {

    public PeerAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindViewHolder(Context context, DataBindHolder<ItemPeerBinding> holder, Peer item) {
        holder.binding.setInspector(new PeerInspector(context, item));
        holder.binding.getRoot().setOnClickListener(v -> {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("player-personaname", item.getPersonaname());
            intent.putExtra("player-account-id", item.getAccountId());
            intent.putExtra("player-avatar-full", item.getAvatarfull());
            context.startActivity(intent);
        });
    }

    @Override
    public ItemPeerBinding createBinding(ViewGroup parent) {
        return ItemPeerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
    }

    @Override
    public void addData(List<Peer> data) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return list == null ? 0 : list.size();
            }

            @Override
            public int getNewListSize() {
                return data == null ? 0 : data.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return list.get(oldItemPosition).getAccountId() == data.get(newItemPosition).getAccountId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return list.get(oldItemPosition).getAccountId() == data.get(newItemPosition).getAccountId();
            }
        });

        list = data;

        diffResult.dispatchUpdatesTo(this);
    }
}