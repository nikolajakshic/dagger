package com.nikola.jakshic.dagger.matchstats.overview

import android.animation.LayoutTransition
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.getDuration
import com.nikola.jakshic.dagger.common.timeElapsed
import com.nikola.jakshic.dagger.databinding.FragmentOverviewBinding
import com.nikola.jakshic.dagger.databinding.ItemMatchStatsCollapsedBinding
import com.nikola.jakshic.dagger.databinding.ItemMatchStatsExpandedBinding
import com.nikola.jakshic.dagger.matchstats.MatchStatsLayout
import com.nikola.jakshic.dagger.matchstats.MatchStatsUI
import com.nikola.jakshic.dagger.matchstats.MatchStatsViewModel
import com.nikola.jakshic.dagger.profile.ProfileFragmentDirections
import com.nikola.jakshic.dagger.util.DotaUtil

// Not using @AndroidEntryPoint, ViewModel is instantiated by parent-fragment.
class OverviewFragment : Fragment(R.layout.fragment_overview) {
    private val viewModel by viewModels<MatchStatsViewModel>(
        ownerProducer = { requireParentFragment() }
    )

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private val STATE_INITIAL = "initial"
    private var initialState = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialState = savedInstanceState?.getBoolean(STATE_INITIAL) ?: true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOverviewBinding.bind(view)

        binding.container.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        if (initialState) {
            // Expand the first item
            for (i in 0 until binding.container.childCount) {
                val child = binding.container.getChildAt(i) as? MatchStatsLayout
                if (child != null) {
                    child.expand()
                    break
                }
            }
            initialState = false
        }

        loadMinimap()
        viewModel.match.observe(viewLifecycleOwner) {
            if (it?.players?.size == 10) {
                bind(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_INITIAL, initialState)
    }

    private fun bind(stats: MatchStatsUI) {
        var playerPosition = 0

        bindMatchStats(stats)
        bindMinimap(stats)

        for (i in 0 until binding.container.childCount) {
            // Player stats data can be bound only on MatchStatsLayout,
            // so we need to ignore other layouts
            val view = binding.container.getChildAt(i) as? MatchStatsLayout ?: continue

            val player = stats.players[playerPosition]
            bindPlayerStats(view, player)

            playerPosition++
        }
    }

    private fun bindPlayerStats(view: ViewGroup, item: MatchStatsUI.PlayerStatsUI) {
        val collapsedBinding = ItemMatchStatsCollapsedBinding.bind(view.getChildAt(0))
        val expandedBinding = ItemMatchStatsExpandedBinding.bind(view.getChildAt(1))

        collapsedBinding.imgHero.load(DotaUtil.getHero(requireContext(), item.heroId))
        collapsedBinding.tvPlayerName.text = getPlayerName(item)
        val playerColor = ContextCompat.getColor(
            requireContext(),
            if (item.playerSlot <= 4) R.color.color_green else R.color.color_red
        )
        collapsedBinding.tvPlayerName.setTextColor(playerColor)
        collapsedBinding.tvKda.text =
            getString(R.string.match_kda, item.kills, item.deaths, item.assists)
        collapsedBinding.tvHeroLevel.text = item.level.toString()
        expandedBinding.tvHeroDamage.text =
            getString(R.string.match_hero_damage, item.heroDamage)
        expandedBinding.tvTowerDamage.text =
            getString(R.string.match_tower_damage, item.towerDamage)
        expandedBinding.tvHeroHealing.text =
            getString(R.string.match_hero_healing, item.heroHealing)
        expandedBinding.tvLastHits.text = getString(R.string.match_last_hits, item.lastHits)
        expandedBinding.tvDenies.text = getString(R.string.match_denies, item.denies)
        expandedBinding.tvObserver.text = item.purchaseWardObserver.toString() + "x"
        expandedBinding.tvSentry.text = item.purchaseWardSentry.toString() + "x"
        expandedBinding.tvGpm.text = getString(R.string.match_gpm, item.goldPerMin)
        expandedBinding.tvXpm.text = getString(R.string.match_xpm, item.xpPerMin)

        collapsedBinding.imgItem0.load(DotaUtil.getItem(requireContext(), item.item0))
        collapsedBinding.imgItem1.load(DotaUtil.getItem(requireContext(), item.item1))
        collapsedBinding.imgItem2.load(DotaUtil.getItem(requireContext(), item.item2))
        collapsedBinding.imgItem3.load(DotaUtil.getItem(requireContext(), item.item3))
        collapsedBinding.imgItem4.load(DotaUtil.getItem(requireContext(), item.item4))
        collapsedBinding.imgItem5.load(DotaUtil.getItem(requireContext(), item.item5))

        expandedBinding.imgBackpack0.load(DotaUtil.getItem(requireContext(), item.backpack0))
        expandedBinding.imgBackpack1.load(DotaUtil.getItem(requireContext(), item.backpack1))
        expandedBinding.imgBackpack2.load(DotaUtil.getItem(requireContext(), item.backpack2))
        expandedBinding.imgItemNeutral.load(DotaUtil.getItem(requireContext(), item.itemNeutral))

        // Having personaName = null means the player has not exposed his data to public,
        // so we don't need to set onClickListener
        if (!TextUtils.isEmpty(item.personaName)) collapsedBinding.tvPlayerName.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.profileAction(accountId = item.id))
        }
    }

    private fun bindMatchStats(item: MatchStatsUI) {
        if (item.isRadiantWin)
            binding.tvRadiantName.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_trophy,
                0
            )
        else
            binding.tvDireName.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_trophy,
                0
            )

        val radiantName =
            if (TextUtils.isEmpty(item.radiantName)) getString(R.string.team_radiant) else item.radiantName
        val direName =
            if (TextUtils.isEmpty(item.direName)) getString(R.string.team_dire) else item.direName
        binding.tvRadiantName.text = radiantName
        binding.tvDireName.text = direName
        binding.containerMatchStatsInfo.tvRadiantScore.text = item.radiantScore.toString()
        binding.containerMatchStatsInfo.tvDireScore.text = item.direScore.toString()
        binding.containerMatchStatsInfo.tvMatchMode.text =
            resources.getString(R.string.match_mode, DotaUtil.mode[item.mode.toInt(), "Unknown"])
        binding.containerMatchStatsInfo.tvMatchSkill.text =
            resources.getString(R.string.match_skill, DotaUtil.skill[item.skill.toInt(), "Unknown"])
        val duration = getDuration(requireContext(), item.duration)
        binding.containerMatchStatsInfo.tvMatchDuration.text =
            resources.getString(R.string.duration, duration)
        val timeElapsed = timeElapsed(requireContext(), item.startTime + item.duration)
        binding.containerMatchStatsInfo.tvMatchTimeElapsed.text =
            resources.getString(R.string.match_ended, timeElapsed)
    }

    private fun bindMinimap(item: MatchStatsUI) {
        // https://wiki.teamfortress.com/wiki/WebAPI/GetMatchDetails
        val radiantTowers = Integer.toBinaryString(item.radiantTowers.toInt()).padStart(11, '0')
        val direTowers = Integer.toBinaryString(item.direTowers.toInt()).padStart(11, '0')
        val radiantBarracks = Integer.toBinaryString(item.radiantBarracks.toInt()).padStart(6, '0')
        val direBarracks = Integer.toBinaryString(item.direBarracks.toInt()).padStart(6, '0')

        // Gray-scale filter for destroyed buildings
        val matrix = ColorMatrix().apply { setSaturation(0F) }
        val filter = ColorMatrixColorFilter(matrix)
        val alpha = 128

        if (item.isRadiantWin) with(binding.containerMinimap.imgDireThrone) {
            colorFilter = filter; imageAlpha = alpha
        } else with(binding.containerMinimap.imgRadiantThrone) {
            colorFilter = filter; imageAlpha = alpha
        }

        if (radiantTowers[10] == '0') with(binding.containerMinimap.imgRadiantTopTier1Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantTowers[9] == '0') with(binding.containerMinimap.imgRadiantTopTier2Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantTowers[8] == '0') with(binding.containerMinimap.imgRadiantTopTier3Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantTowers[1] == '0') with(binding.containerMinimap.imgRadiantTopTier4Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantTowers[7] == '0') with(binding.containerMinimap.imgRadiantMidTier1Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantTowers[6] == '0') with(binding.containerMinimap.imgRadiantMidTier2Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantTowers[5] == '0') with(binding.containerMinimap.imgRadiantMidTier3Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantTowers[4] == '0') with(binding.containerMinimap.imgRadiantBotTier1Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantTowers[3] == '0') with(binding.containerMinimap.imgRadiantBotTier2Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantTowers[2] == '0') with(binding.containerMinimap.imgRadiantBotTier3Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantTowers[0] == '0') with(binding.containerMinimap.imgRadiantBotTier4Tower) {
            colorFilter = filter; imageAlpha = alpha
        }

        if (radiantBarracks[4] == '0') with(binding.containerMinimap.imgRadiantTopRangedRax) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantBarracks[5] == '0') with(binding.containerMinimap.imgRadiantTopMeleeRax) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantBarracks[2] == '0') with(binding.containerMinimap.imgRadiantMidRangedRax) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantBarracks[3] == '0') with(binding.containerMinimap.imgRadiantMidMeleeRax) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantBarracks[0] == '0') with(binding.containerMinimap.imgRadiantBotRangedRax) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (radiantBarracks[1] == '0') with(binding.containerMinimap.imgRadiantBotMeleeRax) {
            colorFilter = filter; imageAlpha = alpha
        }

        if (direTowers[10] == '0') with(binding.containerMinimap.imgDireTopTier1Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direTowers[9] == '0') with(binding.containerMinimap.imgDireTopTier2Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direTowers[8] == '0') with(binding.containerMinimap.imgDireTopTier3Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direTowers[1] == '0') with(binding.containerMinimap.imgDireTopTier4Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direTowers[7] == '0') with(binding.containerMinimap.imgDireMidTier1Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direTowers[6] == '0') with(binding.containerMinimap.imgDireMidTier2Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direTowers[5] == '0') with(binding.containerMinimap.imgDireMidTier3Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direTowers[4] == '0') with(binding.containerMinimap.imgDireBotTier1Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direTowers[3] == '0') with(binding.containerMinimap.imgDireBotTier2Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direTowers[2] == '0') with(binding.containerMinimap.imgDireBotTier3Tower) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direTowers[0] == '0') with(binding.containerMinimap.imgDireBotTier4Tower) {
            colorFilter = filter; imageAlpha = alpha
        }

        if (direBarracks[4] == '0') with(binding.containerMinimap.imgDireTopRangedRax) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direBarracks[5] == '0') with(binding.containerMinimap.imgDireTopMeleeRax) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direBarracks[2] == '0') with(binding.containerMinimap.imgDireMidRangedRax) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direBarracks[3] == '0') with(binding.containerMinimap.imgDireMidMeleeRax) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direBarracks[0] == '0') with(binding.containerMinimap.imgDireBotRangedRax) {
            colorFilter = filter; imageAlpha = alpha
        }
        if (direBarracks[1] == '0') with(binding.containerMinimap.imgDireBotMeleeRax) {
            colorFilter = filter; imageAlpha = alpha
        }
    }

    private fun loadMinimap() {
        binding.containerMinimap.imgMinimap.load(R.drawable.ic_minimap)

        binding.containerMinimap.imgRadiantTopRangedRax.load(R.drawable.ic_radiant_rax)
        binding.containerMinimap.imgRadiantTopMeleeRax.load(R.drawable.ic_radiant_rax)
        binding.containerMinimap.imgRadiantTopTier3Tower.load(R.drawable.ic_radiant_tower)
        binding.containerMinimap.imgRadiantTopTier2Tower.load(R.drawable.ic_radiant_tower)
        binding.containerMinimap.imgRadiantTopTier1Tower.load(R.drawable.ic_radiant_tower)
        binding.containerMinimap.imgRadiantMidRangedRax.load(R.drawable.ic_radiant_rax_angle)
        binding.containerMinimap.imgRadiantMidMeleeRax.load(R.drawable.ic_radiant_rax_angle)
        binding.containerMinimap.imgRadiantMidTier3Tower.load(R.drawable.ic_radiant_tower_angle)
        binding.containerMinimap.imgRadiantMidTier2Tower.load(R.drawable.ic_radiant_tower_angle)
        binding.containerMinimap.imgRadiantMidTier1Tower.load(R.drawable.ic_radiant_tower_angle)
        binding.containerMinimap.imgRadiantBotRangedRax.load(R.drawable.ic_radiant_rax)
        binding.containerMinimap.imgRadiantBotMeleeRax.load(R.drawable.ic_radiant_rax)
        binding.containerMinimap.imgRadiantBotTier3Tower.load(R.drawable.ic_radiant_tower)
        binding.containerMinimap.imgRadiantBotTier2Tower.load(R.drawable.ic_radiant_tower)
        binding.containerMinimap.imgRadiantBotTier1Tower.load(R.drawable.ic_radiant_tower)
        binding.containerMinimap.imgRadiantThrone.load(R.drawable.ic_radiant_throne)
        binding.containerMinimap.imgRadiantTopTier4Tower.load(R.drawable.ic_radiant_tower_angle)
        binding.containerMinimap.imgRadiantBotTier4Tower.load(R.drawable.ic_radiant_tower_angle)

        binding.containerMinimap.imgDireTopRangedRax.load(R.drawable.ic_dire_rax)
        binding.containerMinimap.imgDireTopMeleeRax.load(R.drawable.ic_dire_rax)
        binding.containerMinimap.imgDireTopTier3Tower.load(R.drawable.ic_dire_tower)
        binding.containerMinimap.imgDireTopTier2Tower.load(R.drawable.ic_dire_tower)
        binding.containerMinimap.imgDireTopTier1Tower.load(R.drawable.ic_dire_tower)
        binding.containerMinimap.imgDireMidRangedRax.load(R.drawable.ic_dire_rax_angle)
        binding.containerMinimap.imgDireMidMeleeRax.load(R.drawable.ic_dire_rax_angle)
        binding.containerMinimap.imgDireMidTier3Tower.load(R.drawable.ic_dire_tower_angle)
        binding.containerMinimap.imgDireMidTier2Tower.load(R.drawable.ic_dire_tower_angle)
        binding.containerMinimap.imgDireMidTier1Tower.load(R.drawable.ic_dire_tower_angle)
        binding.containerMinimap.imgDireBotRangedRax.load(R.drawable.ic_dire_rax)
        binding.containerMinimap.imgDireBotMeleeRax.load(R.drawable.ic_dire_rax)
        binding.containerMinimap.imgDireBotTier3Tower.load(R.drawable.ic_dire_tower)
        binding.containerMinimap.imgDireBotTier2Tower.load(R.drawable.ic_dire_tower)
        binding.containerMinimap.imgDireBotTier1Tower.load(R.drawable.ic_dire_tower)
        binding.containerMinimap.imgDireThrone.load(R.drawable.ic_dire_throne)
        binding.containerMinimap.imgDireTopTier4Tower.load(R.drawable.ic_dire_tower_angle)
        binding.containerMinimap.imgDireBotTier4Tower.load(R.drawable.ic_dire_tower_angle)
    }

    private fun getPlayerName(item: MatchStatsUI.PlayerStatsUI) = when {
        !TextUtils.isEmpty(item.name) -> item.name
        !TextUtils.isEmpty(item.personaName) -> item.personaName
        else -> "Unknown"
    }
}