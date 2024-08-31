package com.nikola.jakshic.dagger.profile.matches

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.getDuration
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.common.timeElapsed
import com.nikola.jakshic.dagger.databinding.ItemMatchBinding
import com.nikola.jakshic.dagger.util.DotaUtil

class MatchAdapter(
    private val listener: (matchId: Long) -> Unit,
) : PagingDataAdapter<MatchUI, MatchAdapter.MatchVH>(MATCH_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchVH {
        return MatchVH(parent.inflate(R.layout.item_match))
    }

    override fun onBindViewHolder(holder: MatchVH, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class MatchVH(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemMatchBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                listener(getItem(position)!!.matchId)
            }
        }

        fun bind(item: MatchUI) {
            binding.imgHero.load(item.heroImage)
            binding.tvMatchResult.text =
                if (isWin(item)) {
                    itemView.context.getString(R.string.won)
                } else {
                    itemView.context.getString(
                        R.string.lost,
                    )
                }
            val resultColor = if (isWin(item)) {
                ContextCompat.getColor(itemView.context, R.color.color_green)
            } else {
                ContextCompat.getColor(itemView.context, R.color.color_red)
            }
            binding.tvMatchResult.setTextColor(resultColor)
            binding.tvMatchSkill.text =
                DotaUtil.skill[item.skill.toInt(), itemView.context.getString(R.string.unknown)]
            binding.tvMatchMode.text =
                DotaUtil.mode[item.gameMode.toInt(), itemView.context.getString(R.string.unknown)]
            binding.tvMatchLobby.text =
                DotaUtil.lobby[item.lobbyType.toInt(), itemView.context.getString(R.string.unknown)]
            binding.tvMatchDuration.text = getDuration(itemView.context, item.duration)
            binding.tvMatchTimeElapsed.text =
                timeElapsed(itemView.context, item.startTime + item.duration)
        }

        private fun isWin(item: MatchUI): Boolean {
            if (item.isRadiantWin && item.playerSlot <= 4) return true
            if (!item.isRadiantWin && item.playerSlot > 4) return true
            return false
        }
    }

    companion object {
        val MATCH_COMPARATOR = object : DiffUtil.ItemCallback<MatchUI>() {
            override fun areItemsTheSame(oldItem: MatchUI, newItem: MatchUI): Boolean {
                return oldItem.matchId == newItem.matchId
            }

            override fun areContentsTheSame(oldItem: MatchUI, newItem: MatchUI): Boolean {
                return oldItem == newItem
            }
        }
    }
}
