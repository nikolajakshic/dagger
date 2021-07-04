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
import com.nikola.jakshic.dagger.databinding.ActivityProfileBinding
import com.nikola.jakshic.dagger.util.DotaUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.activity_profile) {
    private val viewModel by viewModels<ProfileViewModel>()
    private val args by navArgs<ProfileFragmentArgs>()

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ActivityProfileBinding.bind(view)

        val id = args.accountId

        // Change the color of the progress bar
        binding.containerHeader.progressBar.indeterminateDrawable.setColorFilter(
            Color.WHITE,
            PorterDuff.Mode.MULTIPLY
        )

        viewModel.getProfile(id)

        viewModel.profile.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.containerHeader.imgPlayerAvatar.load(it.avatarUrl) {
                    transformations(CircleCropTransformation())
                }

                val medal = DotaUtil.getMedal(requireContext(), it.rankTier, it.leaderboardRank)
                val stars = DotaUtil.getStars(requireContext(), it.rankTier, it.leaderboardRank)
                binding.containerHeader.imgRankMedal.load(medal)
                binding.containerHeader.imgRankStars.load(stars)

                val name = if (TextUtils.isEmpty(it.name)) it.personaName else it.name
                binding.collapsingToolbar.title = name
                binding.containerHeader.tvPlayerName.text = name

                binding.containerHeader.tvLeaderboardRank.text =
                    if (it.leaderboardRank != 0L) it.leaderboardRank.toString() else null
                binding.containerHeader.tvPlayerId.text = it.id.toString()
                binding.containerHeader.tvPlayerGames.text = resources.getString(
                    R.string.player_games,
                    (it.wins + it.losses) as Long
                ) // lint is throwing `wrong argument type for formatting argument` error
                binding.containerHeader.tvPlayerWins.text = resources.getString(
                    R.string.player_wins,
                    it.wins as Long
                ) // lint is throwing `wrong argument type for formatting argument` error
                binding.containerHeader.tvPlayerLosses.text = resources.getString(
                    R.string.player_losses,
                    it.losses as Long
                ) // lint is throwing `wrong argument type for formatting argument` error

                val winRate = (it.wins.toDouble() / (it.wins + it.losses)) * 100
                binding.containerHeader.tvPlayerWinRate.text = resources.getString(
                    R.string.player_winrate,
                    winRate as Double
                ) // lint is throwing `wrong argument type for formatting argument` error
            }
        }

        viewModel.bookmark.observe(viewLifecycleOwner) {
            with(binding.containerHeader.btnFollow) {
                if (it == null) {
                    text = getString(R.string.follow)
                    setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                    background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_toolbar_follow_inactive
                    )
                } else {
                    text = getString(R.string.unfollow)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                    background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_toolbar_follow_active
                    )
                }
            }
        }

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> {
                    binding.containerHeader.btnRefresh.isEnabled = false
                    binding.containerHeader.btnRefresh.visibility = View.GONE
                    binding.containerHeader.progressBar.visibility = View.VISIBLE
                }
                else -> {
                    binding.containerHeader.btnRefresh.visibility = View.VISIBLE
                    binding.containerHeader.progressBar.visibility = View.GONE
                    binding.containerHeader.btnRefresh.isEnabled = true
                }
            }
        }

        binding.containerHeader.btnRefresh.setOnClickListener { viewModel.fetchProfile(id) }

        val medalDialog = MedalDialog()
        binding.containerHeader.imgRankMedal.setOnClickListener {
            if (!medalDialog.isAdded) medalDialog.show(
                childFragmentManager,
                null
            )
        }

        // Toolbar is drawn over the medal and refresh button, so we need to register clicks
        // on the toolbar and then pass them to the proper views.
        binding.toolbar.setOnTouchListener { v, event ->
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

        binding.containerHeader.btnFollow.setOnClickListener {
            if (viewModel.bookmark.value == null)
                viewModel.addToBookmark(id)
            else {
                viewModel.removeFromBookmark(id)
            }
        }

        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.adapter = ProfilePagerAdapter(requireContext(), childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}