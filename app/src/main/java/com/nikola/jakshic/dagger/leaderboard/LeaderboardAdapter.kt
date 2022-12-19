package com.nikola.jakshic.dagger.leaderboard

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.databinding.ItemLeaderboardBinding
import com.nikola.jakshic.dagger.leaderboard.LeaderboardAdapter.ViewHolder

class LeaderboardAdapter : ListAdapter<LeaderboardUI, ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_leaderboard))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemLeaderboardBinding.bind(view)

        fun bind(item: LeaderboardUI) {
            binding.tvRank.text = "${item.rank}"
            binding.tvPlayerName.text = item.name
        }
    }

    @Suppress("ClassName")
    private companion object DIFF_CALLBACK : DiffUtil.ItemCallback<LeaderboardUI>() {
        override fun areItemsTheSame(oldItem: LeaderboardUI, newItem: LeaderboardUI): Boolean {
            return false // there is nothing unique about players, so we have nothing to compare
        }

        override fun areContentsTheSame(oldItem: LeaderboardUI, newItem: LeaderboardUI): Boolean {
            return false // there is nothing unique about players, so we have nothing to compare
        }
    }
}
