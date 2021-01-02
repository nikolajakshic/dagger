package com.nikola.jakshic.dagger.competitive

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.common.timeElapsed
import kotlinx.android.synthetic.main.item_competitive.view.*

class CompetitiveAdapter(
    val context: Context,
    val listener: (matchId: Long) -> Unit
) : PagedListAdapter<CompetitiveUI, CompetitiveAdapter.CompetitiveViewHolder>(COMPETITIVE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompetitiveViewHolder {
        return CompetitiveViewHolder(parent.inflate(R.layout.item_competitive))
    }

    override fun onBindViewHolder(holder: CompetitiveViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class CompetitiveViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                listener(getItem(adapterPosition)!!.matchId)
            }
        }

        fun bind(item: CompetitiveUI) {
            val leagueName = if (TextUtils.isEmpty(item.leagueName)) context.getString(R.string.match_unknown_league) else item.leagueName
            val radiantName = if (TextUtils.isEmpty(item.radiantName)) context.getString(R.string.match_unknown_team) else item.radiantName
            val direName = if (TextUtils.isEmpty(item.direName)) context.getString(R.string.match_unknown_team) else item.direName
            val timeElapsed = timeElapsed(context, item.startTime + item.duration)
            val radiantTrophy = if (item.isRadiantWin) R.drawable.ic_trophy else R.drawable.ic_trophy_invisible
            val direTrophy = if (item.isRadiantWin) R.drawable.ic_trophy_invisible else R.drawable.ic_trophy

            with(itemView) {
                tvRadiantName.text = radiantName
                tvRadiantScore.text = item.radiantScore.toString()
                tvDireName.text = direName
                tvDireScore.text = item.direScore.toString()
                tvLeagueName.text = leagueName
                tvTimeElapsed.text = timeElapsed
                tvRadiantName.setCompoundDrawablesWithIntrinsicBounds(radiantTrophy, 0, 0, 0)
                tvDireName.setCompoundDrawablesWithIntrinsicBounds(direTrophy, 0, 0, 0)
            }
        }
    }

    companion object {
        val COMPETITIVE_COMPARATOR = object : DiffUtil.ItemCallback<CompetitiveUI>() {
            override fun areItemsTheSame(oldItem: CompetitiveUI, newItem: CompetitiveUI): Boolean {
                return oldItem.matchId == newItem.matchId
            }

            override fun areContentsTheSame(oldItem: CompetitiveUI, newItem: CompetitiveUI): Boolean {
                return oldItem == newItem
            }
        }
    }
}