package com.nikola.jakshic.dagger.ui.matchStats

import android.animation.LayoutTransition
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.ui.DaggerViewModelFactory
import com.nikola.jakshic.dagger.ui.profile.ProfileActivity
import com.nikola.jakshic.dagger.util.DotaUtil
import com.nikola.jakshic.dagger.vo.MatchStats
import com.nikola.jakshic.dagger.vo.PlayerStats
import com.nikola.jakshic.dagger.vo.Stats
import kotlinx.android.synthetic.main.activity_match_stats.*
import kotlinx.android.synthetic.main.item_match_stats_collapsed.view.*
import kotlinx.android.synthetic.main.item_match_stats_expanded.view.*
import kotlinx.android.synthetic.main.item_match_stats_match_info.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MatchStatsActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: DaggerViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as DaggerApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_stats)

        container.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        val id = intent.getLongExtra("match_id", -1)
        title = "Match $id"
        val viewModel = ViewModelProviders.of(this, factory)[MatchStatsViewModel::class.java]

        viewModel.initialFetch(id)

        viewModel.match.observe(this, Observer {

            if (it?.playerStats?.size == 10) {
                bind(it)
            }
        })
    }

    private fun bind(stats: Stats) {
        var playerPosition = 0

        bindMatchStats(stats.matchStats!!)
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
            Glide.with(this).load(DotaUtil.getHero(context, item.heroId)).transition(withCrossFade()).into(imgHero)
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

            Glide.with(this).load(DotaUtil.getItem(context, item.item0)).transition(withCrossFade()).into(imgItem0)
            Glide.with(this).load(DotaUtil.getItem(context, item.item1)).transition(withCrossFade()).into(imgItem1)
            Glide.with(this).load(DotaUtil.getItem(context, item.item2)).transition(withCrossFade()).into(imgItem2)
            Glide.with(this).load(DotaUtil.getItem(context, item.item3)).transition(withCrossFade()).into(imgItem3)
            Glide.with(this).load(DotaUtil.getItem(context, item.item4)).transition(withCrossFade()).into(imgItem4)
            Glide.with(this).load(DotaUtil.getItem(context, item.item5)).transition(withCrossFade()).into(imgItem5)

            Glide.with(this).load(DotaUtil.getItem(context, item.backpack0)).transition(withCrossFade()).into(imgBackpack0)
            Glide.with(this).load(DotaUtil.getItem(context, item.backpack1)).transition(withCrossFade()).into(imgBackpack1)
            Glide.with(this).load(DotaUtil.getItem(context, item.backpack2)).transition(withCrossFade()).into(imgBackpack2)

            // Having personaName = null means the player has not exposed his data to public,
            // so we don't need to set onClickListener
            if (!TextUtils.isEmpty(item.personaName)) tvPlayerName.setOnClickListener {
                val intent = Intent(this@MatchStatsActivity, ProfileActivity::class.java)
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

        val radiantName = if (TextUtils.isEmpty(item.radiantTeam?.name)) "The Radiant" else item.radiantTeam?.name
        val direName = if (TextUtils.isEmpty(item.direTeam?.name)) "The Dire" else item.direTeam?.name
        tvRadiantName.text = radiantName
        tvDireName.text = direName

        tvRadiantScore.text = item.radiantScore.toString()
        tvDireScore.text = item.direScore.toString()
        tvMatchMode.text = resources.getString(R.string.match_mode, DotaUtil.mode[item.mode, "Unknown"])
        tvMatchSkill.text = resources.getString(R.string.match_skill, DotaUtil.skill[item.skill, "Unknown"])
        tvMatchDuration.text = getDuration(item)
        tvMatchTimePassed.text = getTimePassed(item)
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