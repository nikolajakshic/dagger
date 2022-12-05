package com.nikola.jakshic.dagger.bookmark.player

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.databinding.ItemPlayerBinding
import com.nikola.jakshic.dagger.profile.PlayerUI

// Not renamed because it is shared between Search and Bookmark functionality.
class PlayerAdapter(
    private val listener: (PlayerUI) -> Unit
) : RecyclerView.Adapter<PlayerAdapter.PlayerVH>() {
    private var list: List<PlayerUI>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerVH {
        return PlayerVH(parent.inflate(R.layout.item_player))
    }

    override fun onBindViewHolder(holder: PlayerVH, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun setData(list: List<PlayerUI>?) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class PlayerVH(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemPlayerBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                listener(list!![position])
            }
        }

        fun bind(item: PlayerUI) {
            binding.tvPlayerName.text =
                if (item.name.isNullOrEmpty()) item.personaName else item.name
            binding.tvPlayerId.text = item.id.toString()
            binding.imgAvatar.load(item.avatarUrl) {
                transformations(CircleCropTransformation())
            }
        }
    }
}
