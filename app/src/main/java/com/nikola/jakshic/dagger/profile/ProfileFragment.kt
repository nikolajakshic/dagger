package com.nikola.jakshic.dagger.profile

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.util.DotaUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar_profile.*

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.activity_profile) {
    private val viewModel by viewModels<ProfileViewModel>()
    private val args by navArgs<ProfileFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.accountId

        // Change the color of the progress bar
        progressBar.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)

        viewModel.getProfile(id)

        viewModel.profile.observe(viewLifecycleOwner) {
            if (it != null) {
                imgPlayerAvatar.load(it.avatarUrl) {
                    transformations(CircleCropTransformation())
                }

                val medal = DotaUtil.getMedal(requireContext(), it.rankTier, it.leaderboardRank)
                val stars = DotaUtil.getStars(requireContext(), it.rankTier, it.leaderboardRank)
                imgRankMedal.load(medal)
                imgRankStars.load(stars)

                val name = if (TextUtils.isEmpty(it.name)) it.personaName else it.name
                collapsingToolbar.title = name
                tvPlayerName.text = name

                tvLeaderboardRank.text = if (it.leaderboardRank != 0L) it.leaderboardRank.toString() else null
                tvPlayerId.text = it.id.toString()
                tvPlayerGames.text = resources.getString(R.string.player_games, (it.wins + it.losses) as Long) // lint is throwing `wrong argument type for formatting argument` error
                tvPlayerWins.text = resources.getString(R.string.player_wins, it.wins as Long) // lint is throwing `wrong argument type for formatting argument` error
                tvPlayerLosses.text = resources.getString(R.string.player_losses, it.losses as Long) // lint is throwing `wrong argument type for formatting argument` error

                val winRate = (it.wins.toDouble() / (it.wins + it.losses)) * 100
                tvPlayerWinRate.text = resources.getString(R.string.player_winrate, winRate as Double) // lint is throwing `wrong argument type for formatting argument` error
            }
        }

        viewModel.bookmark.observe(viewLifecycleOwner) {
            with(btnFollow) {
                if (it == null) {
                    text = getString(R.string.follow)
                    setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.button_toolbar_follow_inactive)
                } else {
                    text = getString(R.string.unfollow)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.button_toolbar_follow_active)
                }
            }
        }

        viewModel.status.observe(viewLifecycleOwner) {
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
        }

        btnRefresh.setOnClickListener { viewModel.fetchProfile(id) }

        val medalDialog = MedalDialog()
        imgRankMedal.setOnClickListener { if (!medalDialog.isAdded) medalDialog.show(childFragmentManager, null) }

        // Toolbar is drawn over the medal and refresh button, so we need to register clicks
        // on the toolbar and then pass them to the proper views.
        toolbar.setOnTouchListener { v, event ->
            if (event.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false
            val refreshX = toolbar.width - btnRefresh.width

            val medalMarginLeft = (imgRankMedal.layoutParams as ConstraintLayout.LayoutParams).leftMargin
            val medalMarginTop = (imgRankMedal.layoutParams as ConstraintLayout.LayoutParams).topMargin
            val medalWidth = imgRankMedal.width

            if (event.x >= refreshX && btnRefresh.isEnabled) btnRefresh.callOnClick()
            if (event.y >= medalMarginTop && event.x >= medalMarginLeft && event.x <= (medalWidth + medalMarginLeft)) imgRankMedal.callOnClick()
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
        viewPager.adapter = ProfilePagerAdapter(requireContext(), childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }
}