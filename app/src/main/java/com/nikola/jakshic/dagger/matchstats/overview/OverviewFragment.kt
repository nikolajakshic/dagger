package com.nikola.jakshic.dagger.matchstats.overview

import android.animation.LayoutTransition
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
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
import com.nikola.jakshic.dagger.matchstats.MatchStats
import com.nikola.jakshic.dagger.matchstats.MatchStatsLayout
import com.nikola.jakshic.dagger.matchstats.MatchStatsViewModel
import com.nikola.jakshic.dagger.matchstats.PlayerStats
import com.nikola.jakshic.dagger.matchstats.Stats
import com.nikola.jakshic.dagger.profile.ProfileActivity
import com.nikola.jakshic.dagger.util.DotaUtil
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.item_match_stats_collapsed.view.*
import kotlinx.android.synthetic.main.item_match_stats_expanded.view.*
import kotlinx.android.synthetic.main.item_match_stats_match_info.*
import kotlinx.android.synthetic.main.item_match_stats_minimap.*
import java.util.concurrent.TimeUnit

class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        container.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        if (savedInstanceState == null) {
            // Expand the first item
            for (i in 0 until container.childCount) {
                val child = container.getChildAt(i) as? MatchStatsLayout
                if (child != null) {
                    child.expand()
                    break
                }
            }
        }

        val viewModel = ViewModelProviders.of(activity!!)[MatchStatsViewModel::class.java]

        viewModel.match.observe(this, Observer {
            if (it?.playerStats?.size == 10) {

                bind(it)
            }
        })
    }

    private fun bind(stats: Stats) {
        var playerPosition = 0

        bindMatchStats(stats.matchStats!!)
        bindMinimap(stats.matchStats!!)
        // Sort by player slot, so that first 5 players are from the Radiant Team,
        // and the rest of them are from Dire
        val sortedPlayerStats = stats.playerStats!!.sortedBy { it.playerSlot }

        for (i in 0 until container.childCount) {
            // Player stats data can be bound only on MatchStatsLayout,
            // so we need to ignore other layouts
            val view = container.getChildAt(i) as? MatchStatsLayout ?: continue

            val playerStats = sortedPlayerStats[playerPosition]
            bindPlayerStats(view, playerStats)

            playerPosition++
        }
    }

    private fun bindPlayerStats(view: View, item: PlayerStats) {
        with(view) {
            imgHero.load(DotaUtil.getHero(context, item.heroId))
            tvPlayerName.text = getPlayerName(item)
            val playerColor = ContextCompat.getColor(context, if (item.playerSlot <= 4) R.color.color_green else R.color.color_red)
            tvPlayerName.setTextColor(playerColor)
            tvKda.text = getString(R.string.match_kda, item.kills, item.deaths, item.assists)
            tvHeroLevel.text = item.level.toString()
            tvHeroDamage.text = getString(R.string.match_hero_damage, item.heroDamage)
            tvTowerDamage.text = getString(R.string.match_tower_damage, item.towerDamage)
            tvHeroHealing.text = getString(R.string.match_hero_healing, item.heroHealing)
            tvLastHits.text = getString(R.string.match_last_hits, item.lastHits)
            tvDenies.text = getString(R.string.match_denies, item.denies)
            tvObserver.text = item.purchaseWardObserver.toString() + "x"
            tvSentry.text = item.purchaseWardSentry.toString() + "x"
            tvGpm.text = getString(R.string.match_gpm, item.goldPerMin)
            tvXpm.text = getString(R.string.match_xpm, item.xpPerMin)

            imgItem0.load(DotaUtil.getItem(context, item.item0))
            imgItem1.load(DotaUtil.getItem(context, item.item1))
            imgItem2.load(DotaUtil.getItem(context, item.item2))
            imgItem3.load(DotaUtil.getItem(context, item.item3))
            imgItem4.load(DotaUtil.getItem(context, item.item4))
            imgItem5.load(DotaUtil.getItem(context, item.item5))

            imgBackpack0.load(DotaUtil.getItem(context, item.backpack0))
            imgBackpack1.load(DotaUtil.getItem(context, item.backpack1))
            imgBackpack2.load(DotaUtil.getItem(context, item.backpack2))

            // Having personaName = null means the player has not exposed his data to public,
            // so we don't need to set onClickListener
            if (!TextUtils.isEmpty(item.personaName)) tvPlayerName.setOnClickListener {
                val intent = Intent(activity, ProfileActivity::class.java)
                intent.putExtra("account_id", item.id)
                startActivity(intent)
            }
        }
    }

    private fun bindMatchStats(item: MatchStats) {
        if (item.isRadiantWin)
            tvRadiantName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_trophy, 0)
        else
            tvDireName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_trophy, 0)

        val radiantName = if (TextUtils.isEmpty(item.radiantTeam?.name)) getString(R.string.team_radiant) else item.radiantTeam?.name
        val direName = if (TextUtils.isEmpty(item.direTeam?.name)) getString(R.string.team_dire) else item.direTeam?.name
        tvRadiantName.text = radiantName
        tvDireName.text = direName

        tvRadiantScore.text = item.radiantScore.toString()
        tvDireScore.text = item.direScore.toString()
        tvMatchMode.text = resources.getString(R.string.match_mode, DotaUtil.mode[item.mode, "Unknown"])
        tvMatchSkill.text = resources.getString(R.string.match_skill, DotaUtil.skill[item.skill, "Unknown"])
        tvMatchDuration.text = getDuration(item)
        tvMatchTimePassed.text = getTimePassed(item)
    }

    private fun bindMinimap(item: MatchStats) {
        // https://wiki.teamfortress.com/wiki/WebAPI/GetMatchDetails
        val radiantTowers = Integer.toBinaryString(item.radiantTowers).padStart(11, '0')
        val direTowers = Integer.toBinaryString(item.direTowers).padStart(11, '0')
        val radiantBarracks = Integer.toBinaryString(item.radiantBarracks).padStart(6, '0')
        val direBarracks = Integer.toBinaryString(item.direBarracks).padStart(6, '0')

        // Gray-scale filter for destroyed buildings
        val matrix = ColorMatrix().apply { setSaturation(0F) }
        val filter = ColorMatrixColorFilter(matrix)
        val alpha = 128

        if (item.isRadiantWin) with(imgDireThrone) { colorFilter = filter; imageAlpha = alpha } else with(imgRadiantThrone) { colorFilter = filter; imageAlpha = alpha }

        if (radiantTowers[10] == '0') with(imgRadiantTopTier1Tower) { colorFilter = filter; imageAlpha = alpha }
        if (radiantTowers[9] == '0') with(imgRadiantTopTier2Tower) { colorFilter = filter; imageAlpha = alpha }
        if (radiantTowers[8] == '0') with(imgRadiantTopTier3Tower) { colorFilter = filter; imageAlpha = alpha }
        if (radiantTowers[1] == '0') with(imgRadiantTopTier4Tower) { colorFilter = filter; imageAlpha = alpha }
        if (radiantTowers[7] == '0') with(imgRadiantMidTier1Tower) { colorFilter = filter; imageAlpha = alpha }
        if (radiantTowers[6] == '0') with(imgRadiantMidTier2Tower) { colorFilter = filter; imageAlpha = alpha }
        if (radiantTowers[5] == '0') with(imgRadiantMidTier3Tower) { colorFilter = filter; imageAlpha = alpha }
        if (radiantTowers[4] == '0') with(imgRadiantBotTier1Tower) { colorFilter = filter; imageAlpha = alpha }
        if (radiantTowers[3] == '0') with(imgRadiantBotTier2Tower) { colorFilter = filter; imageAlpha = alpha }
        if (radiantTowers[2] == '0') with(imgRadiantBotTier3Tower) { colorFilter = filter; imageAlpha = alpha }
        if (radiantTowers[0] == '0') with(imgRadiantBotTier4Tower) { colorFilter = filter; imageAlpha = alpha }

        if (radiantBarracks[4] == '0') with(imgRadiantTopRangedRax) { colorFilter = filter; imageAlpha = alpha }
        if (radiantBarracks[5] == '0') with(imgRadiantTopMeleeRax) { colorFilter = filter; imageAlpha = alpha }
        if (radiantBarracks[2] == '0') with(imgRadiantMidRangedRax) { colorFilter = filter; imageAlpha = alpha }
        if (radiantBarracks[3] == '0') with(imgRadiantMidMeleeRax) { colorFilter = filter; imageAlpha = alpha }
        if (radiantBarracks[0] == '0') with(imgRadiantBotRangedRax) { colorFilter = filter; imageAlpha = alpha }
        if (radiantBarracks[1] == '0') with(imgRadiantBotMeleeRax) { colorFilter = filter; imageAlpha = alpha }

        if (direTowers[10] == '0') with(imgDireTopTier1Tower) { colorFilter = filter; imageAlpha = alpha }
        if (direTowers[9] == '0') with(imgDireTopTier2Tower) { colorFilter = filter; imageAlpha = alpha }
        if (direTowers[8] == '0') with(imgDireTopTier3Tower) { colorFilter = filter; imageAlpha = alpha }
        if (direTowers[1] == '0') with(imgDireTopTier4Tower) { colorFilter = filter; imageAlpha = alpha }
        if (direTowers[7] == '0') with(imgDireMidTier1Tower) { colorFilter = filter; imageAlpha = alpha }
        if (direTowers[6] == '0') with(imgDireMidTier2Tower) { colorFilter = filter; imageAlpha = alpha }
        if (direTowers[5] == '0') with(imgDireMidTier3Tower) { colorFilter = filter; imageAlpha = alpha }
        if (direTowers[4] == '0') with(imgDireBotTier1Tower) { colorFilter = filter; imageAlpha = alpha }
        if (direTowers[3] == '0') with(imgDireBotTier2Tower) { colorFilter = filter; imageAlpha = alpha }
        if (direTowers[2] == '0') with(imgDireBotTier3Tower) { colorFilter = filter; imageAlpha = alpha }
        if (direTowers[0] == '0') with(imgDireBotTier4Tower) { colorFilter = filter; imageAlpha = alpha }

        if (direBarracks[4] == '0') with(imgDireTopRangedRax) { colorFilter = filter; imageAlpha = alpha }
        if (direBarracks[5] == '0') with(imgDireTopMeleeRax) { colorFilter = filter; imageAlpha = alpha }
        if (direBarracks[2] == '0') with(imgDireMidRangedRax) { colorFilter = filter; imageAlpha = alpha }
        if (direBarracks[3] == '0') with(imgDireMidMeleeRax) { colorFilter = filter; imageAlpha = alpha }
        if (direBarracks[0] == '0') with(imgDireBotRangedRax) { colorFilter = filter; imageAlpha = alpha }
        if (direBarracks[1] == '0') with(imgDireBotMeleeRax) { colorFilter = filter; imageAlpha = alpha }
    }

    private fun getPlayerName(item: PlayerStats) = when {
        !TextUtils.isEmpty(item.name) -> item.name
        !TextUtils.isEmpty(item.personaName) -> item.personaName
        else -> "Unknown"
    }

    private fun getDuration(item: MatchStats): String {
        val hours = item.duration / (60 * 60)
        val minutes = (item.duration / 60) % 60
        val seconds = item.duration % 60
        if (hours != 0) return resources.getString(R.string.match_duration_with_prefix, hours, minutes, seconds)
        return resources.getString(R.string.match_duration_zero_hours_with_prefix, minutes, seconds)
    }

    private fun getTimePassed(item: MatchStats): String {
        val endTime = TimeUnit.SECONDS.toMillis(item.startTime + item.duration)
        val timePassed = System.currentTimeMillis() - endTime

        val years = TimeUnit.MILLISECONDS.toDays(timePassed) / 365
        val months = TimeUnit.MILLISECONDS.toDays(timePassed) / 30
        val days = TimeUnit.MILLISECONDS.toDays(timePassed)
        val hours = TimeUnit.MILLISECONDS.toHours(timePassed)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timePassed)

        return when {
            years > 0 -> resources.getQuantityString(R.plurals.year_with_prefix, years.toInt(), years)
            months > 0 -> resources.getQuantityString(R.plurals.month_with_prefix, months.toInt(), months)
            days > 0 -> resources.getQuantityString(R.plurals.day_with_prefix, days.toInt(), days)
            hours > 0 -> resources.getQuantityString(R.plurals.hour_with_prefix, hours.toInt(), hours)
            else -> resources.getQuantityString(R.plurals.minute_with_prefix, minutes.toInt(), minutes)
        }
    }
}