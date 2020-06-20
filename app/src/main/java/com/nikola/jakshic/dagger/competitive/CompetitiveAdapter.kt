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
import com.nikola.jakshic.dagger.common.sqldelight.Competitive
import com.nikola.jakshic.dagger.common.timeElapsed
import kotlinx.android.synthetic.main.item_competitive.view.*

class CompetitiveAdapter(
    val context: Context,
    val listener: (matchId: Long?) -> Unit
) : PagedListAdapter<Competitive, CompetitiveAdapter.CompetitiveViewHolder>(COMPETITIVE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompetitiveViewHolder {
        return CompetitiveViewHolder(parent.inflate(R.layout.item_competitive))
    }

    override fun onBindViewHolder(holder: CompetitiveViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class CompetitiveViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                listener(getItem(adapterPosition)!!.match_id)
            }
        }

        fun bind(item: Competitive) {
            val leagueName = if (TextUtils.isEmpty(item.league_name)) context.getString(R.string.match_unknown_league) else item.league_name
            val radiantName = if (TextUtils.isEmpty(item.radiant_name)) context.getString(R.string.match_unknown_team) else item.radiant_name
            val direName = if (TextUtils.isEmpty(item.dire_name)) context.getString(R.string.match_unknown_team) else item.dire_name
            val timeElapsed = timeElapsed(context, item.start_time + item.duration)
            val radiantTrophy = if (item.radiant_win) R.drawable.ic_trophy else R.drawable.ic_trophy_invisible
            val direTrophy = if (item.radiant_win) R.drawable.ic_trophy_invisible else R.drawable.ic_trophy

            with(itemView) {
                tvRadiantName.text = radiantName
                tvRadiantScore.text = item.radiant_score.toString()
                tvDireName.text = direName
                tvDireScore.text = item.dire_score.toString()
                tvLeagueName.text = leagueName
                tvTimeElapsed.text = timeElapsed
                tvRadiantName.setCompoundDrawablesWithIntrinsicBounds(radiantTrophy, 0, 0, 0)
                tvDireName.setCompoundDrawablesWithIntrinsicBounds(direTrophy, 0, 0, 0)
            }
        }
    }

    companion object {
        val COMPETITIVE_COMPARATOR = object : DiffUtil.ItemCallback<Competitive>() {
            override fun areItemsTheSame(oldItem: Competitive, newItem: Competitive): Boolean {
                return oldItem.match_id == newItem.match_id
            }

            override fun areContentsTheSame(oldItem: Competitive, newItem: Competitive): Boolean {
                return oldItem == newItem
            }
        }
    }
}