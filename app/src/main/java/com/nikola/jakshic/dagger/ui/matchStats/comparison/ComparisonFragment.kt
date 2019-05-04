package com.nikola.jakshic.dagger.ui.matchstats.comparison

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.di.GlideApp
import com.nikola.jakshic.dagger.ui.matchstats.MatchStatsViewModel
import com.nikola.jakshic.dagger.util.DotaUtil
import com.nikola.jakshic.dagger.vo.PlayerStats
import com.nikola.jakshic.dagger.vo.Stats
import com.nikola.jakshic.spiderchart.SpiderData
import kotlinx.android.synthetic.main.fragment_comparison.*
import java.util.concurrent.TimeUnit

class ComparisonFragment : Fragment(), ComparisonDialog.ComparisonClickListener {

    private val MAX_HERO_DAMAGE_PER_MINUTE = 800F
    private val MAX_TOWER_DAMAGE_PER_MINUTE = 300F
    private val MAX_LAST_HITS_PER_MINUTE = 12F
    private val MAX_DENIES_PER_MINUTE = 3F
    private val MAX_GOLD_PER_MINUTE = 700F
    private val MAX_EXPERIENCE_PER_MINUTE = 600F

    private var leftPlayerIndex = 0
    private var rightPlayerIndex = 5

    private var SELECTED_PLAYER = -1
    private val SELECTED_PLAYER_LEFT = 0
    private val SELECTED_PLAYER_RIGHT = 1

    private var stats: Stats? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comparison, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(activity!!)[MatchStatsViewModel::class.java]

        if (savedInstanceState != null) {
            leftPlayerIndex = savedInstanceState.getInt("player1", 0)
            rightPlayerIndex = savedInstanceState.getInt("player2", 5)
            SELECTED_PLAYER = savedInstanceState.getInt("selectedPlayer", -1)
        }

        viewModel.match.observe(this, Observer { stats ->
            if (stats?.playerStats?.size == 10) {

                // Sort by player slot, so that first 5 players are from the Radiant Team,
                // and the rest of them are from Dire
                stats.playerStats = stats.playerStats!!.sortedBy { it.playerSlot }
                this.stats = stats
                setData(stats)

                var dialog: ComparisonDialog? = null

                fun setupDialog() {
                    if (dialog?.isAdded != true) {
                        dialog = ComparisonDialog.newInstance(
                                leftPlayerIndex,
                                rightPlayerIndex,
                                stats.playerStats!!.map { it.heroId }.toList() as ArrayList<Int>)
                        dialog?.setTargetFragment(this, 301)
                        dialog?.show(fragmentManager, null)
                    }
                }

                player1.setOnClickListener {
                    SELECTED_PLAYER = SELECTED_PLAYER_LEFT
                    setupDialog()
                }

                player2.setOnClickListener {
                    SELECTED_PLAYER = SELECTED_PLAYER_RIGHT
                    setupDialog()
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("player1", leftPlayerIndex)
        outState.putInt("player2", rightPlayerIndex)
        outState.putInt("selectedPlayer", SELECTED_PLAYER)
        super.onSaveInstanceState(outState)
    }

    private fun setData(stats: Stats) {
        val labels = listOf("LH", "DN", "TD", "GPM", "XPM", "HD")

        val player1 = stats.playerStats!![leftPlayerIndex]
        val player2 = stats.playerStats!![rightPlayerIndex]

        val durationInMinutes = TimeUnit.SECONDS.toMinutes(stats.matchStats!!.duration.toLong())

        val entries1 = floatArrayOf(
                100 * player1.lastHits.toFloat() / (MAX_LAST_HITS_PER_MINUTE * durationInMinutes),
                100 * player1.denies.toFloat() / (MAX_DENIES_PER_MINUTE * durationInMinutes),
                100 * player1.towerDamage.toFloat() / (MAX_TOWER_DAMAGE_PER_MINUTE * durationInMinutes),
                100 * player1.goldPerMin.toFloat() / (MAX_GOLD_PER_MINUTE),
                100 * player1.xpPerMin.toFloat() / (MAX_EXPERIENCE_PER_MINUTE),
                100 * player1.heroDamage / (MAX_HERO_DAMAGE_PER_MINUTE * durationInMinutes))

        val entries2 = floatArrayOf(
                100 * player2.lastHits.toFloat() / (MAX_LAST_HITS_PER_MINUTE * durationInMinutes),
                100 * player2.denies.toFloat() / (MAX_DENIES_PER_MINUTE * durationInMinutes),
                100 * player2.towerDamage.toFloat() / (MAX_TOWER_DAMAGE_PER_MINUTE * durationInMinutes),
                100 * player2.goldPerMin.toFloat() / (MAX_GOLD_PER_MINUTE),
                100 * player2.xpPerMin.toFloat() / (MAX_EXPERIENCE_PER_MINUTE),
                100 * player2.heroDamage / (MAX_HERO_DAMAGE_PER_MINUTE * durationInMinutes))

        val color1 = ContextCompat.getColor(context!!, R.color.comparison_player1)
        val color2 = ContextCompat.getColor(context!!, R.color.comparison_player2)

        spiderChart.setData(listOf(SpiderData(entries1, color1), SpiderData(entries2, color2)))
        spiderChart.setLabels(labels)
        spiderChart.setRotationAngle(120f)
        spiderChart.refresh()

        GlideApp.with(this).load(DotaUtil.getHero(context!!, player1.heroId)).into(imgPlayer1Hero)
        GlideApp.with(this).load(DotaUtil.getHero(context!!, player2.heroId)).into(imgPlayer2Hero)

        tvPlayer1Name.text = getPlayerName(player1)
        tvPlayer2Name.text = getPlayerName(player2)

        tvPlayer1Kda.text = getString(R.string.match_kda, player1.kills, player1.deaths, player1.assists)
        tvPlayer2Kda.text = getString(R.string.match_kda, player2.kills, player2.deaths, player2.assists)

        GlideApp.with(this).load(DotaUtil.getItem(context!!, player1.item0)).into(imgPlayer1Item0)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player1.item1)).into(imgPlayer1Item1)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player1.item2)).into(imgPlayer1Item2)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player1.item3)).into(imgPlayer1Item3)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player1.item4)).into(imgPlayer1Item4)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player1.item5)).into(imgPlayer1Item5)

        GlideApp.with(this).load(DotaUtil.getItem(context!!, player1.backpack0)).into(imgPlayer1Backpack0)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player1.backpack1)).into(imgPlayer1Backpack1)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player1.backpack2)).into(imgPlayer1Backpack2)

        GlideApp.with(this).load(DotaUtil.getItem(context!!, player2.item0)).into(imgPlayer2Item0)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player2.item1)).into(imgPlayer2Item1)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player2.item2)).into(imgPlayer2Item2)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player2.item3)).into(imgPlayer2Item3)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player2.item4)).into(imgPlayer2Item4)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player2.item5)).into(imgPlayer2Item5)

        GlideApp.with(this).load(DotaUtil.getItem(context!!, player2.backpack0)).into(imgPlayer2Backpack0)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player2.backpack1)).into(imgPlayer2Backpack1)
        GlideApp.with(this).load(DotaUtil.getItem(context!!, player2.backpack2)).into(imgPlayer2Backpack2)
    }

    private fun getPlayerName(item: PlayerStats) = when {
        !TextUtils.isEmpty(item.name) -> item.name
        !TextUtils.isEmpty(item.personaName) -> item.personaName
        else -> "Unknown"
    }

    override fun onClick(playerIndex: Int) {
        if (stats != null) {
            when (SELECTED_PLAYER) {
                SELECTED_PLAYER_RIGHT -> rightPlayerIndex = playerIndex
                SELECTED_PLAYER_LEFT -> leftPlayerIndex = playerIndex
            }
            setData(stats!!)
        }
    }
}