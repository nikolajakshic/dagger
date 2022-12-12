package com.nikola.jakshic.dagger.profile

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.tabs.TabLayoutMediator
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.extensions.getColor
import com.nikola.jakshic.dagger.common.extensions.getDrawable
import com.nikola.jakshic.dagger.databinding.ActivityProfileBinding
import com.nikola.jakshic.dagger.util.DotaUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG_MEDAL_DIALOG = "medal-dialog"

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.activity_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ActivityProfileBinding.bind(view)
        val viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        val accountId = ProfileFragmentArgs.fromBundle(requireArguments()).accountId

        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.adapter = ProfilePagerAdapter(accountId, /* fragment */ this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val title = when (position) {
                0 -> getString(R.string.matches)
                1 -> getString(R.string.heroes)
                2 -> getString(R.string.peers)
                else -> throw IllegalStateException("Found more than 3 tabs.")
            }
            tab.text = title
        }.attach()

        // Change the color of the progress bar
        binding.containerHeader.progressBar.indeterminateDrawable.setColorFilter(
            Color.WHITE,
            PorterDuff.Mode.MULTIPLY
        )
        binding.containerHeader.btnRefresh.setOnClickListener { viewModel.fetchProfile() }
        binding.containerHeader.imgRankMedal.setOnClickListener {
            if (childFragmentManager.findFragmentByTag(TAG_MEDAL_DIALOG) == null) {
                MedalDialog().showNow(childFragmentManager, TAG_MEDAL_DIALOG)
            }
        }
        // Toolbar is drawn over the medal and refresh button, so we need to register clicks
        // on the toolbar and then pass them to the proper views.
        binding.toolbar.setOnTouchListener { _, event ->
            if (event.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false
            val refreshX = binding.toolbar.width - binding.containerHeader.btnRefresh.width

            val medalMarginLeft =
                (binding.containerHeader.imgRankMedal.layoutParams as ConstraintLayout.LayoutParams).leftMargin
            val medalMarginTop =
                (binding.containerHeader.imgRankMedal.layoutParams as ConstraintLayout.LayoutParams).topMargin
            val medalWidth = binding.containerHeader.imgRankMedal.width

            if (event.x >= refreshX && binding.containerHeader.btnRefresh.isEnabled) binding.containerHeader.btnRefresh.callOnClick()
            if (event.y >= medalMarginTop && event.x >= medalMarginLeft && event.x <= (medalWidth + medalMarginLeft)) binding.containerHeader.imgRankMedal.callOnClick()
            false
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.player.collectLatest { player ->
                    if (player == null) {
                        return@collectLatest
                    }
                    binding.containerHeader.imgPlayerAvatar.load(player.avatarUrl) {
                        transformations(CircleCropTransformation())
                    }

                    val medal = DotaUtil.getMedal(
                        requireContext(),
                        player.rankTier,
                        player.leaderboardRank
                    )
                    val stars = DotaUtil.getStars(
                        requireContext(),
                        player.rankTier,
                        player.leaderboardRank
                    )
                    binding.containerHeader.imgRankMedal.load(medal)
                    binding.containerHeader.imgRankStars.load(stars)

                    val name = if (player.name.isNullOrEmpty()) player.personaName else player.name
                    binding.collapsingToolbar.title = name
                    binding.containerHeader.tvPlayerName.text = name

                    binding.containerHeader.tvLeaderboardRank.text =
                        if (player.leaderboardRank != 0L) {
                            player.leaderboardRank.toString()
                        } else {
                            null
                        }
                    binding.containerHeader.tvPlayerId.text = player.id.toString()
                    binding.containerHeader.tvPlayerGames.text = getString(
                        R.string.player_games,
                        (player.wins + player.losses)
                    )
                    binding.containerHeader.tvPlayerWins.text = getString(
                        R.string.player_wins,
                        player.wins
                    )
                    binding.containerHeader.tvPlayerLosses.text = getString(
                        R.string.player_losses,
                        player.losses
                    )

                    val winRate = (player.wins.toDouble() / (player.wins + player.losses)) * 100
                    binding.containerHeader.tvPlayerWinRate.text = getString(
                        R.string.player_winrate,
                        winRate
                    )
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isBookmarked.collectLatest { isBookmarked ->
                    with(binding.containerHeader.btnFollow) {
                        if (isBookmarked) {
                            text = getString(R.string.unfollow)
                            setTextColor(getColor(R.color.colorAccent))
                            background = getDrawable(R.drawable.button_toolbar_follow_active)
                        } else {
                            text = getString(R.string.follow)
                            setTextColor(getColor(android.R.color.white))
                            background = getDrawable(R.drawable.button_toolbar_follow_inactive)
                        }
                    }
                    binding.containerHeader.btnFollow.setOnClickListener {
                        if (isBookmarked) {
                            viewModel.removeFromBookmark()
                        } else {
                            viewModel.addToBookmark()
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collectLatest { isLoading ->
                    binding.containerHeader.btnRefresh.isEnabled = !isLoading
                    binding.containerHeader.btnRefresh.isVisible = !isLoading
                    binding.containerHeader.progressBar.isVisible = isLoading
                }
            }
        }
    }
}
