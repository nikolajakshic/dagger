package com.nikola.jakshic.dagger.bookmark.match

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.common.timeElapsed
import com.nikola.jakshic.dagger.databinding.ItemBookmarkMatchBinding

class MatchBookmarkAdapter(
    private val onClick: (matchId: Long) -> Unit,
    private val onHold: (matchId: Long, note: String?) -> Unit
) : ListAdapter<MatchBookmarkUI, MatchBookmarkAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_bookmark_match))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemBookmarkMatchBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                val matchId = getItem(position).matchId
                onClick(matchId)
            }
            itemView.setOnLongClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener true
                }
                val item = getItem(position)
                onHold(item.matchId, item.note)
                true
            }
        }

        fun bind(item: MatchBookmarkUI) {
            // TODO is this even possible?
            if (item.matchStats.players.size != 10) {
                return
            }
            val context = itemView.context

            //noinspection SetTextI18n
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
            binding.imgPlayer1.load(item.matchStats.players[0].heroImage)
            binding.imgPlayer2.load(item.matchStats.players[1].heroImage)
            binding.imgPlayer3.load(item.matchStats.players[2].heroImage)
            binding.imgPlayer4.load(item.matchStats.players[3].heroImage)
            binding.imgPlayer5.load(item.matchStats.players[4].heroImage)
            binding.imgPlayer6.load(item.matchStats.players[5].heroImage)
            binding.imgPlayer7.load(item.matchStats.players[6].heroImage)
            binding.imgPlayer8.load(item.matchStats.players[7].heroImage)
            binding.imgPlayer9.load(item.matchStats.players[8].heroImage)
            binding.imgPlayer10.load(item.matchStats.players[9].heroImage)
        }
    }

    @Suppress("ClassName")
    private companion object DIFF_CALLBACK : DiffUtil.ItemCallback<MatchBookmarkUI>() {
        override fun areItemsTheSame(oldItem: MatchBookmarkUI, newItem: MatchBookmarkUI): Boolean {
            return oldItem.matchId == newItem.matchId
        }

        override fun areContentsTheSame(
            oldItem: MatchBookmarkUI,
            newItem: MatchBookmarkUI
        ): Boolean {
            return oldItem == newItem
        }
    }
}
