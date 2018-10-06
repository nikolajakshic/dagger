package com.nikola.jakshic.dagger.ui.profile.peers

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.inflate
import com.nikola.jakshic.dagger.vo.Peer
import kotlinx.android.synthetic.main.item_peer.view.*

class PeerAdapter(val listener: (Long) -> Unit) : RecyclerView.Adapter<PeerAdapter.PeerVH>() {

    private var list: List<Peer>? = null
    private val options = RequestOptions().circleCrop()

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
                Glide.with(this)
                        .load(item.avatarfull)
                        .apply(options)
                        .transition(withCrossFade())
                        .into(imgPeerAvatar)

                tvPeerName.text = item.personaname
                tvPeerGamesWith.text = item.withGames.toString()
                val winRate = (item.withWin.toDouble() / item.withGames) * 100
                tvPeerWinRate.text = context.resources.getString(R.string.peer_winrate, winRate)
            }
        }
    }
}