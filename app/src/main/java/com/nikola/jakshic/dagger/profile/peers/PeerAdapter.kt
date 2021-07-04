package com.nikola.jakshic.dagger.profile.peers

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.databinding.ItemPeerBinding

class PeerAdapter(
    private val listener: (Long) -> Unit
) : RecyclerView.Adapter<PeerAdapter.PeerVH>() {
    private var list: List<PeerUI>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerVH {
        return PeerVH(parent.inflate(R.layout.item_peer))
    }

    override fun onBindViewHolder(holder: PeerVH, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun addData(newList: List<PeerUI>?) {
        list = newList
        notifyDataSetChanged()
    }

    inner class PeerVH(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemPeerBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                listener(list!![position].peerId)
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
}