package com.nikola.jakshic.dagger.matchstats.comparison

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.databinding.FragmentComparisonBinding
import com.nikola.jakshic.dagger.matchstats.MatchStatsUI
import com.nikola.jakshic.dagger.matchstats.MatchStatsViewModel
import com.nikola.jakshic.dagger.util.DotaUtil
import com.nikola.jakshic.spiderchart.SpiderData
import java.util.concurrent.TimeUnit

// Not using @AndroidEntryPoint, ViewModel is instantiated by parent-fragment.
class ComparisonFragment :
    Fragment(R.layout.fragment_comparison),
    ComparisonDialog.ComparisonClickListener {
    private val viewModel by viewModels<MatchStatsViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    private var _binding: FragmentComparisonBinding? = null
    private val binding get() = _binding!!

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leftPlayerIndex = savedInstanceState?.getInt("player1", 0) ?: 0
        rightPlayerIndex = savedInstanceState?.getInt("player2", 5) ?: 5
        SELECTED_PLAYER = savedInstanceState?.getInt("selectedPlayer", -1) ?: -1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentComparisonBinding.bind(view)

        viewModel.match.observe(viewLifecycleOwner) { stats ->
            if (stats?.players?.size == 10) {
                this.stats = stats
                setData(stats)

                var dialog: ComparisonDialog? = null

                fun setupDialog() {
                    if (dialog?.isAdded != true) {
                        dialog = ComparisonDialog.newInstance(
                            leftPlayerIndex,
                            rightPlayerIndex,
                            stats.players.map { it.heroId }.toList().toLongArray()
                        )
                        dialog?.setTargetFragment(this, 301)
                        dialog?.show(parentFragmentManager, null)
                    }
                }

                binding.player1.setOnClickListener {
                    SELECTED_PLAYER = SELECTED_PLAYER_LEFT
                    setupDialog()
                }

                binding.player2.setOnClickListener {
                    SELECTED_PLAYER = SELECTED_PLAYER_RIGHT
                    setupDialog()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("player1", leftPlayerIndex)
        outState.putInt("player2", rightPlayerIndex)
        outState.putInt("selectedPlayer", SELECTED_PLAYER)
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
            100 * player1.heroDamage / (MAX_HERO_DAMAGE_PER_MINUTE * durationInMinutes)
        )

        val entries2 = floatArrayOf(
            100 * player2.lastHits.toFloat() / (MAX_LAST_HITS_PER_MINUTE * durationInMinutes),
            100 * player2.denies.toFloat() / (MAX_DENIES_PER_MINUTE * durationInMinutes),
            100 * player2.towerDamage.toFloat() / (MAX_TOWER_DAMAGE_PER_MINUTE * durationInMinutes),
            100 * player2.goldPerMin.toFloat() / (MAX_GOLD_PER_MINUTE),
            100 * player2.xpPerMin.toFloat() / (MAX_EXPERIENCE_PER_MINUTE),
            100 * player2.heroDamage / (MAX_HERO_DAMAGE_PER_MINUTE * durationInMinutes)
        )

        val color1 = ContextCompat.getColor(requireContext(), R.color.comparison_player1)
        val color2 = ContextCompat.getColor(requireContext(), R.color.comparison_player2)

        binding.spiderChart.setData(
            listOf(
                SpiderData(entries1, color1),
                SpiderData(entries2, color2)
            )
        )
        binding.spiderChart.setLabels(labels)
        binding.spiderChart.setRotationAngle(120f)
        binding.spiderChart.refresh()

        binding.imgPlayer1Hero.load(DotaUtil.getHero(requireContext(), player1.heroId))
        binding.imgPlayer2Hero.load(DotaUtil.getHero(requireContext(), player2.heroId))

        binding.tvPlayer1Name.text = getPlayerName(player1)
        binding.tvPlayer2Name.text = getPlayerName(player2)

        binding.tvPlayer1Kda.text =
            getString(R.string.match_kda, player1.kills, player1.deaths, player1.assists)
        binding.tvPlayer2Kda.text =
            getString(R.string.match_kda, player2.kills, player2.deaths, player2.assists)

        binding.imgPlayer1Item0.load(DotaUtil.getItem(requireContext(), player1.item0))
        binding.imgPlayer1Item1.load(DotaUtil.getItem(requireContext(), player1.item1))
        binding.imgPlayer1Item2.load(DotaUtil.getItem(requireContext(), player1.item2))
        binding.imgPlayer1Item3.load(DotaUtil.getItem(requireContext(), player1.item3))
        binding.imgPlayer1Item4.load(DotaUtil.getItem(requireContext(), player1.item4))
        binding.imgPlayer1Item5.load(DotaUtil.getItem(requireContext(), player1.item5))

        binding.imgPlayer1Backpack0.load(DotaUtil.getItem(requireContext(), player1.backpack0))
        binding.imgPlayer1Backpack1.load(DotaUtil.getItem(requireContext(), player1.backpack1))
        binding.imgPlayer1Backpack2.load(DotaUtil.getItem(requireContext(), player1.backpack2))
        binding.imgPlayer1ItemNeutral.load(DotaUtil.getItem(requireContext(), player1.itemNeutral))

        binding.imgPlayer2Item0.load(DotaUtil.getItem(requireContext(), player2.item0))
        binding.imgPlayer2Item1.load(DotaUtil.getItem(requireContext(), player2.item1))
        binding.imgPlayer2Item2.load(DotaUtil.getItem(requireContext(), player2.item2))
        binding.imgPlayer2Item3.load(DotaUtil.getItem(requireContext(), player2.item3))
        binding.imgPlayer2Item4.load(DotaUtil.getItem(requireContext(), player2.item4))
        binding.imgPlayer2Item5.load(DotaUtil.getItem(requireContext(), player2.item5))

        binding.imgPlayer2Backpack0.load(DotaUtil.getItem(requireContext(), player2.backpack0))
        binding.imgPlayer2Backpack1.load(DotaUtil.getItem(requireContext(), player2.backpack1))
        binding.imgPlayer2Backpack2.load(DotaUtil.getItem(requireContext(), player2.backpack2))
        binding.imgPlayer2ItemNeutral.load(DotaUtil.getItem(requireContext(), player2.itemNeutral))
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
