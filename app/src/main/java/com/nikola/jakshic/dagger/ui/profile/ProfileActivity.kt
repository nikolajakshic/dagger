package com.nikola.jakshic.dagger.ui.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.ui.DaggerViewModelFactory
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar_profile.*
import javax.inject.Inject

class ProfileActivity : AppCompatActivity() {

    @Inject lateinit var factory: DaggerViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as DaggerApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val viewModel = ViewModelProviders.of(this, factory)[ProfileViewModel::class.java]

        setSupportActionBar(toolbar)

        val id = intent.getLongExtra("account_id", -1)

        viewModel.getProfile(id)

        viewModel.profile.observe(this, Observer {
            if (it != null) {
                Glide.with(this).load(it.avatarUrl).into(imgPlayerAvatar)

                val rankMedalDrawable = getRankMedalDrawable(this, it.rankTier, it.leaderboardRank)
                val rankStarsDrawable = getRankStarsDrawable(this, it.rankTier, it.leaderboardRank)
                imgRankMedal.setImageDrawable(rankMedalDrawable)
                imgRankStars.setImageDrawable(rankStarsDrawable)

                val name = if (TextUtils.isEmpty(it.name)) it.personaName else it.name
                collapsingToolbar.title = name
                tvPlayerName.text = name

                tvLeaderboardRank.text = if (it.leaderboardRank != 0) it.leaderboardRank.toString() else null
                tvPlayerId.text = it.id.toString()
                tvPlayerGames.text = resources.getString(R.string.player_games, it.wins + it.losses)
                tvPlayerWins.text = resources.getString(R.string.player_wins, it.wins)
                tvPlayerLosses.text = resources.getString(R.string.player_losses, it.losses)

                val winRate = (it.wins.toDouble() / (it.wins + it.losses)) * 100
                tvPlayerWinRate.text = resources.getString(R.string.player_winrate, winRate)
            }
        })

        viewModel.bookmark.observe(this, Observer {
            with(btnFollow) {
                if (it == null) {
                    text = "Follow"
                    setTextColor(ContextCompat.getColor(this@ProfileActivity, android.R.color.white))
                    background = ContextCompat.getDrawable(this@ProfileActivity, R.drawable.button_toolbar_follow_inactive)
                } else {
                    text = "Unfollow"
                    setTextColor(ContextCompat.getColor(this@ProfileActivity, R.color.colorAccent))
                    background = ContextCompat.getDrawable(this@ProfileActivity, R.drawable.button_toolbar_follow_active)
                }
            }
        })

        btnFollow.setOnClickListener {
            if (viewModel.bookmark.value == null)
                viewModel.addToBookmark(id)
            else {
                viewModel.removeFromBookmark(id)
            }
        }

        viewPager.offscreenPageLimit = 2
        viewPager.adapter = ProfilePagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    // TODO Once DotaUtil is converted to Kotlin, we can move this method there
    private fun getRankMedalDrawable(context: Context, rankTier: Int, leaderBoardRank: Int): Drawable? {
        val resource = context.resources
        val packageName = context.packageName
        var drawableName = "ic_rank_"

        drawableName = when {
            rankTier == 0 -> drawableName + "0"
            leaderBoardRank in 1..10 -> drawableName + "7c"
            leaderBoardRank in 11..100 -> drawableName + "7b"
            leaderBoardRank in 101..1000 -> drawableName + "7a"
            else -> drawableName + rankTier / 10
        }
        val resourceId = resource.getIdentifier(drawableName, "drawable", packageName)

        return try {
            ContextCompat.getDrawable(context, resourceId)
        } catch (e: Exception) {
            null // resources not found
        }
    }

    // TODO Once DotaUtil is converted to Kotlin, we can move this method there
    private fun getRankStarsDrawable(context: Context, rankTier: Int, leaderBoardRank: Int): Drawable? {
        val resource = context.resources
        val packageName = context.packageName
        var drawableName = "ic_rank_star_"

        drawableName = when {
        // Resources.getIdentifier throws Exception if name param is null,
        // so we need to return empty String
            leaderBoardRank in 1..1000 -> "" // top 1000 players have special medals without stars
            else -> drawableName + rankTier % 10
        }
        val resourceId = resource.getIdentifier(drawableName, "drawable", packageName)

        return try {
            ContextCompat.getDrawable(context, resourceId)
        } catch (e: Exception) {
            null // resources not found
        }
    }
}