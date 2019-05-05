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
import kotlinx.android.synthetic.main.item_competitive.view.*
import java.util.concurrent.TimeUnit

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
                listener(getItem(adapterPosition)!!.matchId)
            }
        }

        fun bind(item: Competitive) {
            val leagueName = if (TextUtils.isEmpty(item.leagueName)) context.getString(R.string.match_unknown_league) else item.leagueName
            val radiantName = if (TextUtils.isEmpty(item.radiantName)) context.getString(R.string.match_unknown_team) else item.radiantName
            val direName = if (TextUtils.isEmpty(item.direName)) context.getString(R.string.match_unknown_team) else item.direName
            val timePassed = getTimePassed(item)
            val radiantTrophy = if (item.isRadiantWin) R.drawable.ic_trophy else R.drawable.ic_trophy_invisible
            val direTrophy = if (item.isRadiantWin) R.drawable.ic_trophy_invisible else R.drawable.ic_trophy

            with(itemView) {
                tvRadiantName.text = radiantName
                tvRadiantScore.text = item.radiantScore.toString()
                tvDireName.text = direName
                tvDireScore.text = item.direScore.toString()
                tvLeagueName.text = leagueName
                tvTimePassed.text = timePassed
                tvRadiantName.setCompoundDrawablesWithIntrinsicBounds(radiantTrophy, 0, 0, 0)
                tvDireName.setCompoundDrawablesWithIntrinsicBounds(direTrophy, 0, 0, 0)
            }
        }

        private fun getTimePassed(item: Competitive): String? {
            val endTime = TimeUnit.SECONDS.toMillis(item.startTime + item.duration)
            val timePassed = System.currentTimeMillis() - endTime

            val years = TimeUnit.MILLISECONDS.toDays(timePassed) / 365
            val months = TimeUnit.MILLISECONDS.toDays(timePassed) / 30
            val days = TimeUnit.MILLISECONDS.toDays(timePassed)
            val hours = TimeUnit.MILLISECONDS.toHours(timePassed)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(timePassed)

            return when {
                years > 0 -> context.resources?.getQuantityString(R.plurals.year, years.toInt(), years)
                months > 0 -> context.resources?.getQuantityString(R.plurals.month, months.toInt(), months)
                days > 0 -> context.resources?.getQuantityString(R.plurals.day, days.toInt(), days)
                hours > 0 -> context.resources?.getQuantityString(R.plurals.hour, hours.toInt(), hours)
                else -> context.resources?.getQuantityString(R.plurals.minute, minutes.toInt(), minutes)
            }
        }
    }

    companion object {
        val COMPETITIVE_COMPARATOR = object : DiffUtil.ItemCallback<Competitive>() {
            override fun areItemsTheSame(oldItem: Competitive, newItem: Competitive): Boolean {
                return oldItem.matchId == newItem.matchId
            }

            override fun areContentsTheSame(oldItem: Competitive, newItem: Competitive): Boolean {
                return oldItem == newItem
            }
        }
    }
}