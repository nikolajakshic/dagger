package com.nikola.jakshic.dagger.leaderboard

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import kotlinx.android.synthetic.main.item_leaderboard.view.*

class LeaderboardAdapter : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardVH>() {

    private var list: List<LeaderboardUI>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardVH {
        return LeaderboardVH(parent.inflate(R.layout.item_leaderboard))
    }

    override fun onBindViewHolder(holder: LeaderboardVH, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun addData(data: List<LeaderboardUI>?) {
        list = data
        notifyDataSetChanged()
    }

    class LeaderboardVH(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: LeaderboardUI) {
            with(itemView) {
                tvRank.text = "${adapterPosition + 1}."
                tvPlayerName.text = item.name
            }
        }
    }
}