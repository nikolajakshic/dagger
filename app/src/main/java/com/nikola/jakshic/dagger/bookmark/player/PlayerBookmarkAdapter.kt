package com.nikola.jakshic.dagger.bookmark.player

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.transform.CircleCropTransformation
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import kotlinx.android.synthetic.main.item_player.view.*

class PlayerBookmarkAdapter(
    private val listener: (PlayerBookmarkUI) -> Unit
) : RecyclerView.Adapter<PlayerBookmarkAdapter.PlayerVH>() {

    private var list: List<PlayerBookmarkUI>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerVH {
        return PlayerVH(parent.inflate(R.layout.item_player))
    }

    override fun onBindViewHolder(holder: PlayerVH, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun addData(newList: List<PlayerBookmarkUI>?) {
        list = newList
        notifyDataSetChanged()
    }

    inner class PlayerVH(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener { listener(list!![adapterPosition]) }
        }

        fun bind(item: PlayerBookmarkUI) {
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