package com.nikola.jakshic.dagger.bookmark.player

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.databinding.ItemPlayerBinding
import com.nikola.jakshic.dagger.profile.PlayerUI

class PlayerAdapter(
    private val listener: (PlayerUI) -> Unit,
) : ListAdapter<PlayerUI, PlayerAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_player))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemPlayerBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                listener(getItem(position))
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

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlayerUI>() {
            override fun areItemsTheSame(oldItem: PlayerUI, newItem: PlayerUI): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PlayerUI, newItem: PlayerUI): Boolean {
                return oldItem == newItem
            }
        }
    }
}
