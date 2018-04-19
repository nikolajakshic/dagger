package com.nikola.jakshic.dagger.diffcallback;

import android.support.v7.util.DiffUtil;

import com.nikola.jakshic.dagger.model.Peer;

public class PeerDiffCallback extends DiffUtil.ItemCallback<Peer> {

    @Override
    public boolean areItemsTheSame(Peer oldItem, Peer newItem) {
        return oldItem.getPeerId() == newItem.getPeerId();
    }

    @Override
    public boolean areContentsTheSame(Peer oldItem, Peer newItem) {
        return oldItem.equals(newItem);
    }
}