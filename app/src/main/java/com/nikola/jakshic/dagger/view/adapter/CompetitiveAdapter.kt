package com.nikola.jakshic.dagger.view.adapter

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.inflate
import com.nikola.jakshic.dagger.model.Competitive
import kotlinx.android.synthetic.main.item_competitive.view.*
import java.util.concurrent.TimeUnit

class CompetitiveAdapter(val context: Context,
                          callback: DiffUtil.ItemCallback<Competitive>/*,
                          val listener: (matchId: Long?) -> Unit*/)
    : PagedListAdapter<Competitive, CompetitiveAdapter.CompetitiveViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompetitiveViewHolder {
        return CompetitiveViewHolder(parent.inflate(R.layout.item_competitive))
    }

    override fun onBindViewHolder(holder: CompetitiveViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class CompetitiveViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            /*   itemView.setOnClickListener {
                   listener(getItem(adapterPosition)!!.matchId)
               }*/
        }

        fun bind(item: Competitive) {
            val leagueName = if (TextUtils.isEmpty(item.leagueName)) "Unknown League" else item.leagueName
            val radiantName = if (TextUtils.isEmpty(item.radiantName)) "Unknown Team" else item.radiantName
            val direName = if (TextUtils.isEmpty(item.direName)) "Unknown Team" else item.direName
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

        private fun getTimePassed(item: Competitive): String {
            val endTime = TimeUnit.SECONDS.toMillis(item.startTime + item.duration)
            val timePassed = System.currentTimeMillis() - endTime

            val years = TimeUnit.MILLISECONDS.toDays(timePassed) / 365
            val months = TimeUnit.MILLISECONDS.toDays(timePassed) / 30
            val days = TimeUnit.MILLISECONDS.toDays(timePassed)
            val hours = TimeUnit.MILLISECONDS.toHours(timePassed)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(timePassed)

            return when {
                years > 0 -> context.resources.getQuantityString(R.plurals.year, years.toInt(), years)
                months > 0 -> context.resources.getQuantityString(R.plurals.month, months.toInt(), months)
                days > 0 -> context.resources.getQuantityString(R.plurals.day, days.toInt(), days)
                hours > 0 -> context.resources.getQuantityString(R.plurals.hour, hours.toInt(), hours)
                else -> context.resources.getQuantityString(R.plurals.minute, minutes.toInt(), minutes)
            }
        }
    }
}