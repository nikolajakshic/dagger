package com.nikola.jakshic.dagger.bookmark.match

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.common.timeElapsed
import com.nikola.jakshic.dagger.databinding.ItemBookmarkMatchBinding
import com.nikola.jakshic.dagger.util.DotaUtil

class MatchBookmarkAdapter(
    private val onClick: (matchId: Long) -> Unit,
    private val onHold: (note: String, matchId: Long) -> Unit
) : RecyclerView.Adapter<MatchBookmarkAdapter.MatchBookmarkVH>() {
    private var list: List<MatchBookmarkUI>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchBookmarkVH {
        return MatchBookmarkVH(parent.inflate(R.layout.item_bookmark_match))
    }

    override fun onBindViewHolder(holder: MatchBookmarkVH, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun setData(list: List<MatchBookmarkUI>?) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class MatchBookmarkVH(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemBookmarkMatchBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                val matchId = list?.get(position)?.matchStats?.matchId ?: throw RuntimeException()
                onClick(matchId)
            }
            itemView.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener true
                }
                val matchId = list?.get(position)?.matchStats?.matchId ?: throw RuntimeException()
                val note = list?.get(position)?.note ?: ""
                onHold(note, matchId)
                true
            }
        }

        fun bind(item: MatchBookmarkUI) {
            if (item.matchStats.players.size != 10) {
                return
            }
            val context = itemView.context

            binding.tvMatchId.text = "ID ${item.matchStats.matchId}"
            binding.tvRadiantScore.text = "${item.matchStats.radiantScore}"
            binding.tvDireScore.text = "${item.matchStats.direScore}"
            val endTime = item.matchStats.startTime + item.matchStats.duration
            binding.tvTimeElapsed.text = timeElapsed(context, endTime)
            binding.tvMatchNote.text = item.note ?: context.getString(R.string.note_touch_and_hold)
            if (item.matchStats.isRadiantWin) {
                binding.imgWinnerRadiant.visibility = View.VISIBLE
                binding.imgWinnerDire.visibility = View.INVISIBLE
            } else {
                binding.imgWinnerDire.visibility = View.VISIBLE
                binding.imgWinnerRadiant.visibility = View.INVISIBLE
            }
            binding.imgPlayer1.load(DotaUtil.getHero(context, item.matchStats.players[0].heroId))
            binding.imgPlayer2.load(DotaUtil.getHero(context, item.matchStats.players[1].heroId))
            binding.imgPlayer3.load(DotaUtil.getHero(context, item.matchStats.players[2].heroId))
            binding.imgPlayer4.load(DotaUtil.getHero(context, item.matchStats.players[3].heroId))
            binding.imgPlayer5.load(DotaUtil.getHero(context, item.matchStats.players[4].heroId))
            binding.imgPlayer6.load(DotaUtil.getHero(context, item.matchStats.players[5].heroId))
            binding.imgPlayer7.load(DotaUtil.getHero(context, item.matchStats.players[6].heroId))
            binding.imgPlayer8.load(DotaUtil.getHero(context, item.matchStats.players[7].heroId))
            binding.imgPlayer9.load(DotaUtil.getHero(context, item.matchStats.players[8].heroId))
            binding.imgPlayer10.load(DotaUtil.getHero(context, item.matchStats.players[9].heroId))
        }
    }
}