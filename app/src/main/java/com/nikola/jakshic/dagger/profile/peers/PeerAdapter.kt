package com.nikola.jakshic.dagger.profile.peers

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.di.GlideApp
import kotlinx.android.synthetic.main.item_peer.view.*

class PeerAdapter(val listener: (Long) -> Unit) : RecyclerView.Adapter<PeerAdapter.PeerVH>() {

    private var list: List<Peer>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerVH {
        return PeerVH(parent.inflate(R.layout.item_peer))
    }

    override fun onBindViewHolder(holder: PeerVH, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun addData(newList: List<Peer>?) {
        list = newList
        notifyDataSetChanged()
    }

    inner class PeerVH(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener { listener(list!![adapterPosition].peerId) }
        }

        fun bind(item: Peer) {
            with(itemView) {
                GlideApp.with(this).load(item.avatarfull).circleCrop().into(imgPeerAvatar)
                tvPeerName.text = item.personaname
                tvPeerGamesWith.text = item.withGames.toString()
                val winRate = (item.withWin.toDouble() / item.withGames) * 100
                tvPeerWinRate.text = context.resources.getString(R.string.peer_winrate, winRate)
            }
        }
    }
}