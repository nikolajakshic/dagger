package com.nikola.jakshic.dagger.profile.heroes

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.util.DotaUtil
import kotlinx.android.synthetic.main.item_hero.view.*

class HeroAdapter(val listener: (Long) -> Unit) : RecyclerView.Adapter<HeroAdapter.HeroVH>() {

    private var list: List<HeroUI>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroVH {
        return HeroVH(parent.inflate(R.layout.item_hero))
    }

    override fun onBindViewHolder(holder: HeroVH, position: Int) {
        holder.bind(list!![position])
    }

    fun addData(newList: List<HeroUI>?) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemCount() = list?.size ?: 0

    inner class HeroVH(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener { listener(list!![adapterPosition].heroId) }
        }

        fun bind(item: HeroUI) {
            with(itemView) {
                imgHero.load(DotaUtil.getHero(context, item.heroId))
                tvGamesPlayed.text = item.gamesPlayed.toString()
                val winRate = if (item.gamesPlayed != 0L) (item.gamesWon.toFloat() / item.gamesPlayed) * 100 else 0f
                tvHeroWinRate.text = context.resources.getString(R.string.hero_winrate, winRate)
                setPercentageBar(viewHeroWinRateBar, winRate)
                tvHeroWinLose.text = context.resources.getString(R.string.hero_win_loss,
                    item.gamesWon, item.gamesPlayed - item.gamesWon)
            }
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
}