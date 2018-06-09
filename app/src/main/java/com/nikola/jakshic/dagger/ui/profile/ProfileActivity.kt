package com.nikola.jakshic.dagger.ui.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.ui.DaggerViewModelFactory
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.util.DotaUtil
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

        // Change the color of the progress bar
        progressBar.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)

        viewModel.getProfile(id)

        viewModel.profile.observe(this, Observer {
            if (it != null) {
                val options = RequestOptions().circleCrop()
                Glide.with(this).load(it.avatarUrl).apply(options).into(imgPlayerAvatar)

                val medal = DotaUtil.getMedal(this, it.rankTier, it.leaderboardRank)
                val stars = DotaUtil.getStars(this, it.rankTier, it.leaderboardRank)
                Glide.with(this).load(medal).into(imgRankMedal)
                Glide.with(this).load(stars).into(imgRankStars)

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

        viewModel.status.observe(this, Observer {
            when (it) {
                Status.LOADING -> {
                    btnRefresh.isEnabled = false
                    btnRefresh.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                else -> {
                    btnRefresh.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    btnRefresh.isEnabled = true
                }
            }
        })

        btnRefresh.setOnClickListener { viewModel.fetchProfile(id) }

        // Toolbar is drawn over the medal and refresh button, so we need to register clicks
        // on the toolbar and then pass them to the proper views.
        toolbar.setOnTouchListener { v, event ->
            if (event.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false
            val refreshX = toolbar.width - btnRefresh.width
            if (event.x >= refreshX && btnRefresh.isEnabled) btnRefresh.callOnClick()
            false
        }

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
}