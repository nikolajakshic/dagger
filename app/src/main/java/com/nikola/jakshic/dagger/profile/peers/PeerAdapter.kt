package com.nikola.jakshic.dagger.profile.peers

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.databinding.ItemPeerBinding

class PeerAdapter(
    private val listener: (Long) -> Unit
) : ListAdapter<PeerUI, PeerAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_peer))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemPeerBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                listener(getItem(position).peerId)
            }
        }

        fun bind(item: PeerUI) {
            binding.imgPeerAvatar.load(item.avatarfull) {
                transformations(CircleCropTransformation())
            }
            binding.tvPeerName.text = item.personaname
            binding.tvPeerGamesWith.text = item.withGames.toString()
            val winRate = (item.withWin.toDouble() / item.withGames) * 100
            binding.tvPeerWinRate.text =
                itemView.context.resources.getString(R.string.peer_winrate, winRate)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PeerUI>() {
            override fun areItemsTheSame(oldItem: PeerUI, newItem: PeerUI): Boolean {
                return oldItem.peerId == newItem.peerId
            }

            override fun areContentsTheSame(oldItem: PeerUI, newItem: PeerUI): Boolean {
                return oldItem == newItem
            }
        }
    }
}
