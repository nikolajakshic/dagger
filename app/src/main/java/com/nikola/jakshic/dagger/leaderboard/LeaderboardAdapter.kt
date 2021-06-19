package com.nikola.jakshic.dagger.leaderboard

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.databinding.ItemLeaderboardBinding

class LeaderboardAdapter : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardVH>() {
    private var list: List<LeaderboardUI>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardVH {
        return LeaderboardVH(parent.inflate(R.layout.item_leaderboard))
    }

    override fun onBindViewHolder(holder: LeaderboardVH, position: Int) {
        holder.bind(list!![position], position)
    }

    override fun getItemCount() = list?.size ?: 0

    fun setData(list: List<LeaderboardUI>?) {
        this.list = list
        notifyDataSetChanged()
    }

    class LeaderboardVH(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemLeaderboardBinding.bind(view)

        fun bind(item: LeaderboardUI, position: Int) {
            binding.tvRank.text = "${position + 1}."
            binding.tvPlayerName.text = item.name
        }
    }
}