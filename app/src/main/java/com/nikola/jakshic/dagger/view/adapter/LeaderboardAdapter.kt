package com.nikola.jakshic.dagger.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.inflate
import com.nikola.jakshic.dagger.model.Leaderboard
import kotlinx.android.synthetic.main.item_leaderboard.view.*

class LeaderboardAdapter : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardVH>() {

    private var list: List<Leaderboard>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardVH {
        return LeaderboardVH(parent.inflate(R.layout.item_leaderboard))
    }

    override fun onBindViewHolder(holder: LeaderboardVH, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount() = list?.size ?: 0

    fun addData(data: List<Leaderboard>?){
        list = data
        notifyDataSetChanged()
    }

    inner class LeaderboardVH(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Leaderboard) {
            with(itemView) {
                tvRank.text = "${adapterPosition+1}."
                tvPlayerName.text = item.name
            }
        }
    }
}