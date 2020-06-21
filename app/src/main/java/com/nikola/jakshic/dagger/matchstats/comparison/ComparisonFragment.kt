package com.nikola.jakshic.dagger.matchstats.comparison

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.matchstats.MatchStatsUI
import com.nikola.jakshic.dagger.matchstats.MatchStatsViewModel
import com.nikola.jakshic.dagger.util.DotaUtil
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

    private var stats: MatchStatsUI? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comparison, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(requireActivity())[MatchStatsViewModel::class.java]

        if (savedInstanceState != null) {
            leftPlayerIndex = savedInstanceState.getInt("player1", 0)
            rightPlayerIndex = savedInstanceState.getInt("player2", 5)
            SELECTED_PLAYER = savedInstanceState.getInt("selectedPlayer", -1)
        }

        viewModel.match.observe(viewLifecycleOwner, Observer { stats ->
            if (stats?.players?.size == 10) {
                this.stats = stats
                setData(stats)

                var dialog: ComparisonDialog? = null

                fun setupDialog() {
                    if (dialog?.isAdded != true) {
                        dialog = ComparisonDialog.newInstance(
                            leftPlayerIndex,
                            rightPlayerIndex,
                            stats.players.map { it.heroId }.toList().toLongArray())
                        dialog?.setTargetFragment(this, 301)
                        dialog?.show(parentFragmentManager, null)
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

    private fun setData(stats: MatchStatsUI) {
        val labels = listOf("LH", "DN", "TD", "GPM", "XPM", "HD")

        val player1 = stats.players[leftPlayerIndex]
        val player2 = stats.players[rightPlayerIndex]

        val durationInMinutes = TimeUnit.SECONDS.toMinutes(stats.duration)

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

        val color1 = ContextCompat.getColor(requireContext(), R.color.comparison_player1)
        val color2 = ContextCompat.getColor(requireContext(), R.color.comparison_player2)

        spiderChart.setData(listOf(SpiderData(entries1, color1), SpiderData(entries2, color2)))
        spiderChart.setLabels(labels)
        spiderChart.setRotationAngle(120f)
        spiderChart.refresh()

        imgPlayer1Hero.load(DotaUtil.getHero(requireContext(), player1.heroId))
        imgPlayer2Hero.load(DotaUtil.getHero(requireContext(), player2.heroId))

        tvPlayer1Name.text = getPlayerName(player1)
        tvPlayer2Name.text = getPlayerName(player2)

        tvPlayer1Kda.text = getString(R.string.match_kda, player1.kills, player1.deaths, player1.assists)
        tvPlayer2Kda.text = getString(R.string.match_kda, player2.kills, player2.deaths, player2.assists)

        imgPlayer1Item0.load(DotaUtil.getItem(requireContext(), player1.item0))
        imgPlayer1Item1.load(DotaUtil.getItem(requireContext(), player1.item1))
        imgPlayer1Item2.load(DotaUtil.getItem(requireContext(), player1.item2))
        imgPlayer1Item3.load(DotaUtil.getItem(requireContext(), player1.item3))
        imgPlayer1Item4.load(DotaUtil.getItem(requireContext(), player1.item4))
        imgPlayer1Item5.load(DotaUtil.getItem(requireContext(), player1.item5))

        imgPlayer1Backpack0.load(DotaUtil.getItem(requireContext(), player1.backpack0))
        imgPlayer1Backpack1.load(DotaUtil.getItem(requireContext(), player1.backpack1))
        imgPlayer1Backpack2.load(DotaUtil.getItem(requireContext(), player1.backpack2))
        imgPlayer1ItemNeutral.load(DotaUtil.getItem(requireContext(), player1.itemNeutral))

        imgPlayer2Item0.load(DotaUtil.getItem(requireContext(), player2.item0))
        imgPlayer2Item1.load(DotaUtil.getItem(requireContext(), player2.item1))
        imgPlayer2Item2.load(DotaUtil.getItem(requireContext(), player2.item2))
        imgPlayer2Item3.load(DotaUtil.getItem(requireContext(), player2.item3))
        imgPlayer2Item4.load(DotaUtil.getItem(requireContext(), player2.item4))
        imgPlayer2Item5.load(DotaUtil.getItem(requireContext(), player2.item5))

        imgPlayer2Backpack0.load(DotaUtil.getItem(requireContext(), player2.backpack0))
        imgPlayer2Backpack1.load(DotaUtil.getItem(requireContext(), player2.backpack1))
        imgPlayer2Backpack2.load(DotaUtil.getItem(requireContext(), player2.backpack2))
        imgPlayer2ItemNeutral.load(DotaUtil.getItem(requireContext(), player2.itemNeutral))
    }

    private fun getPlayerName(item: MatchStatsUI.PlayerStatsUI) = when {
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