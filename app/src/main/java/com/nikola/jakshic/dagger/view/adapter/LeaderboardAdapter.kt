package com.nikola.jakshic.dagger.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.model.Leaderboard
import kotlinx.android.synthetic.main.item_leaderboard.view.*

class LeaderboardAdapter : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardVH>() {

    private var list: List<Leaderboard>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardVH {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_leaderboard, parent, false)
        return LeaderboardVH(view)
    }

    override fun getItemCount() = list?.size ?: 0

    fun addData(data: List<Leaderboard>?){
        list = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: LeaderboardVH, position: Int) {
        holder.bind(list!![position], position)
    }

    inner class LeaderboardVH(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Leaderboard, position: Int) {
            with(view) {
                textLeaderboardRank.text = "${position+1}."
                textLeaderboardName.text = item.name
            }
        }
    }
}