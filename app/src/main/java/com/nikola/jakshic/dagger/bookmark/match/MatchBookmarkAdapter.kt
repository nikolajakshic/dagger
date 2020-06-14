package com.nikola.jakshic.dagger.bookmark.match

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.matchstats.MatchStats
import com.nikola.jakshic.dagger.util.DotaUtil
import kotlinx.android.synthetic.main.item_bookmark_match.view.*
import java.util.concurrent.TimeUnit

class MatchBookmarkAdapter(
    private val onClick: (matchId: Long) -> Unit,
    private val onHold: (note: String, matchId: Long) -> Unit
) : RecyclerView.Adapter<MatchBookmarkAdapter.MatchBookmarkVH>() {
    private var list: List<MatchesNotes>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchBookmarkVH {
        return MatchBookmarkVH(parent.inflate(R.layout.item_bookmark_match))
    }

    override fun onBindViewHolder(holder: MatchBookmarkVH, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun addData(newList: List<MatchesNotes>?) {
        list = newList
        notifyDataSetChanged()
    }

    inner class MatchBookmarkVH(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                val matchId = list?.get(adapterPosition)?.matchStats?.matchId ?: -1
                onClick(matchId)
            }
            itemView.setOnLongClickListener {
                val matchId = list?.get(adapterPosition)?.matchStats?.matchId ?: -1
                val note = list?.get(adapterPosition)?.note ?: ""
                onHold(note, matchId)
                true
            }
        }

        fun bind(item: MatchesNotes) {
            with(itemView) {
                if (item.playerStats?.size != 10) {
                    return
                }
                // Sort by player slot, so that first 5 players are from the Radiant Team,
                // and the rest of them are from Dire
                val sortedPlayerStats = item.playerStats!!.sortedBy { it.playerSlot } // TODO sort in sql
                tvMatchId.text = "ID ${item.matchStats!!.matchId}"
                tvRadiantScore.text = "${item.matchStats!!.radiantScore}"
                tvDireScore.text = "${item.matchStats!!.direScore}"
                tvTimePassed.text = getTimePassed(item.matchStats!!)
                tvMatchNote.text = item.note ?: context.getString(R.string.note_touch_and_hold)
                if (item.matchStats!!.isRadiantWin) {
                    imgWinnerRadiant.visibility = View.VISIBLE
                    imgWinnerDire.visibility = View.INVISIBLE
                } else {
                    imgWinnerDire.visibility = View.VISIBLE
                    imgWinnerRadiant.visibility = View.INVISIBLE
                }
                imgPlayer1.load(DotaUtil.getHero(context, sortedPlayerStats[0].heroId))
                imgPlayer2.load(DotaUtil.getHero(context, sortedPlayerStats[1].heroId))
                imgPlayer3.load(DotaUtil.getHero(context, sortedPlayerStats[2].heroId))
                imgPlayer4.load(DotaUtil.getHero(context, sortedPlayerStats[3].heroId))
                imgPlayer5.load(DotaUtil.getHero(context, sortedPlayerStats[4].heroId))
                imgPlayer6.load(DotaUtil.getHero(context, sortedPlayerStats[5].heroId))
                imgPlayer7.load(DotaUtil.getHero(context, sortedPlayerStats[6].heroId))
                imgPlayer8.load(DotaUtil.getHero(context, sortedPlayerStats[7].heroId))
                imgPlayer9.load(DotaUtil.getHero(context, sortedPlayerStats[8].heroId))
                imgPlayer10.load(DotaUtil.getHero(context, sortedPlayerStats[9].heroId))
            }
        }

        private fun getTimePassed(item: MatchStats): String? {
            val endTime = TimeUnit.SECONDS.toMillis(item.startTime + item.duration)
            val timePassed = System.currentTimeMillis() - endTime

            val years = TimeUnit.MILLISECONDS.toDays(timePassed) / 365
            val months = TimeUnit.MILLISECONDS.toDays(timePassed) / 30
            val days = TimeUnit.MILLISECONDS.toDays(timePassed)
            val hours = TimeUnit.MILLISECONDS.toHours(timePassed)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(timePassed)

            return when {
                years > 0 -> itemView.context.resources?.getQuantityString(R.plurals.year, years.toInt(), years)
                months > 0 -> itemView.context.resources?.getQuantityString(R.plurals.month, months.toInt(), months)
                days > 0 -> itemView.context.resources?.getQuantityString(R.plurals.day, days.toInt(), days)
                hours > 0 -> itemView.context.resources?.getQuantityString(R.plurals.hour, hours.toInt(), hours)
                else -> itemView.context.resources?.getQuantityString(R.plurals.minute, minutes.toInt(), minutes)
            }
        }
    }
}