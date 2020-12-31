package com.nikola.jakshic.dagger.profile.matches

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.getDuration
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.common.timeElapsed
import com.nikola.jakshic.dagger.util.DotaUtil
import kotlinx.android.synthetic.main.item_match.view.*

class MatchAdapter(
    val listener: (Long) -> Unit
) : PagedListAdapter<MatchUI, MatchAdapter.MatchVH>(MATCH_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchVH {
        return MatchVH(parent.inflate(R.layout.item_match))
    }

    override fun onBindViewHolder(holder: MatchVH, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class MatchVH(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener { listener(getItem(adapterPosition)!!.matchId) }
        }

        fun bind(item: MatchUI) {
            with(itemView) {
                imgHero.load(DotaUtil.getHero(context, item.heroId))
                tvMatchResult.text = if (isWin(item)) context.getString(R.string.won) else context.getString(R.string.lost)
                val resultColor = if (isWin(item))
                    ContextCompat.getColor(context, R.color.color_green)
                else
                    ContextCompat.getColor(context, R.color.color_red)
                tvMatchResult.setTextColor(resultColor)
                tvMatchSkill.text = DotaUtil.skill[item.skill.toInt(), context.getString(R.string.unknown)]
                tvMatchMode.text = DotaUtil.mode[item.gameMode.toInt(), context.getString(R.string.unknown)]
                tvMatchLobby.text = DotaUtil.lobby[item.lobbyType.toInt(), context.getString(R.string.unknown)]
                tvMatchDuration.text = getDuration(context, item.duration)
                tvMatchTimeElapsed.text = timeElapsed(context, item.startTime + item.duration)
            }
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