package com.nikola.jakshic.dagger.profile.heroes

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.databinding.ItemHeroBinding

class HeroAdapter(
    private val listener: (Long) -> Unit
) : ListAdapter<HeroUI, HeroAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_hero))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemHeroBinding.bind(view)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                listener(getItem(position).heroId)
            }
        }

        fun bind(item: HeroUI) {
            binding.imgHero.load(item.imagePath)
            binding.tvGamesPlayed.text = item.gamesPlayed.toString()
            val winRate =
                if (item.gamesPlayed != 0L) (item.gamesWon.toFloat() / item.gamesPlayed) * 100 else 0f
            binding.tvHeroWinRate.text =
                itemView.context.resources.getString(R.string.hero_winrate, winRate)
            setPercentageBar(binding.viewHeroWinRateBar, winRate)
            binding.tvHeroWinLose.text = itemView.context.resources.getString(
                R.string.hero_win_loss,
                item.gamesWon,
                item.gamesPlayed - item.gamesWon
            )
        }

        private fun setPercentageBar(view: View, winRate: Float) {
            var width = 85
            val scale = view.context.resources.displayMetrics.density

            width = (width * scale + 0.5F).toInt() // convert from dp to pixels

            val params = view.layoutParams

            params.width = (width * (winRate / 100)).toInt()

            view.layoutParams = params
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HeroUI>() {
            override fun areItemsTheSame(oldItem: HeroUI, newItem: HeroUI): Boolean {
                return oldItem.heroId == newItem.heroId
            }

            override fun areContentsTheSame(oldItem: HeroUI, newItem: HeroUI): Boolean {
                return oldItem == newItem
            }
        }
    }
}
