package com.nikola.jakshic.dagger.competitive

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.common.timeElapsed
import com.nikola.jakshic.dagger.competitive.CompetitiveAdapter.CompetitiveViewHolder
import com.nikola.jakshic.dagger.databinding.ItemCompetitiveBinding

class CompetitiveAdapter(
    private val listener: (matchId: Long) -> Unit,
) : PagingDataAdapter<CompetitiveUI, CompetitiveViewHolder>(COMPETITIVE_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompetitiveViewHolder {
        return CompetitiveViewHolder(parent.inflate(R.layout.item_competitive))
    }

    override fun onBindViewHolder(holder: CompetitiveViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class CompetitiveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCompetitiveBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                listener(getItem(position)!!.matchId)
            }
        }

        fun bind(item: CompetitiveUI) {
            val context = itemView.context

            val leagueName = when {
                item.leagueName.isNullOrEmpty() -> context.getString(R.string.match_unknown_league)
                else -> item.leagueName
            }
            val radiantName = when {
                item.radiantName.isNullOrEmpty() -> context.getString(R.string.match_unknown_team)
                else -> item.radiantName
            }
            val direName = when {
                item.direName.isNullOrEmpty() -> context.getString(R.string.match_unknown_team)
                else -> item.direName
            }
            val timeElapsed = timeElapsed(context, item.startTime + item.duration)
            val radiantTrophy = when {
                item.isRadiantWin -> R.drawable.ic_trophy
                else -> R.drawable.ic_trophy_invisible
            }
            val direTrophy = when {
                item.isRadiantWin -> R.drawable.ic_trophy_invisible
                else -> R.drawable.ic_trophy
            }

            binding.tvRadiantName.text = radiantName
            binding.tvRadiantScore.text = item.radiantScore.toString()
            binding.tvDireName.text = direName
            binding.tvDireScore.text = item.direScore.toString()
            binding.tvLeagueName.text = leagueName
            binding.tvTimeElapsed.text = timeElapsed
            binding.tvRadiantName.setCompoundDrawablesWithIntrinsicBounds(radiantTrophy, 0, 0, 0)
            binding.tvDireName.setCompoundDrawablesWithIntrinsicBounds(direTrophy, 0, 0, 0)
        }
    }

    companion object {
        private val COMPETITIVE_COMPARATOR = object : DiffUtil.ItemCallback<CompetitiveUI>() {
            override fun areItemsTheSame(old: CompetitiveUI, new: CompetitiveUI): Boolean {
                return old.matchId == new.matchId
            }

            override fun areContentsTheSame(old: CompetitiveUI, new: CompetitiveUI): Boolean {
                return old == new
            }
        }
    }
}
