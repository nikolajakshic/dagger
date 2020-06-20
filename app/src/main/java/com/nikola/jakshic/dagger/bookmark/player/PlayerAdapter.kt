package com.nikola.jakshic.dagger.bookmark.player

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.profile.PlayerUI
import kotlinx.android.synthetic.main.item_player.view.*

// Not renamed because it is shared between Search and Bookmark functionality.
class PlayerAdapter(val listener: (PlayerUI) -> Unit) : RecyclerView.Adapter<PlayerAdapter.PlayerVH>() {

    private var list: List<PlayerUI>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerVH {
        return PlayerVH(parent.inflate(R.layout.item_player))
    }

    override fun onBindViewHolder(holder: PlayerVH, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun addData(newList: List<PlayerUI>?) {
        list = newList
        notifyDataSetChanged()
    }

    inner class PlayerVH(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener { listener(list!![adapterPosition]) }
        }

        fun bind(item: PlayerUI) {
            with(itemView) {
                tvPlayerName.text = if (TextUtils.isEmpty(item.name)) item.personaName else item.name
                tvPlayerId.text = item.id.toString()
                imgAvatar.load(item.avatarUrl) {
                    transformations(CircleCropTransformation())
                }
            }
        }
    }
}