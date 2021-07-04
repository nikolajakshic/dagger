package com.nikola.jakshic.dagger.matchstats

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.databinding.ActivityMatchStatsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchStatsFragment : Fragment(R.layout.activity_match_stats) {
    private val viewModel by viewModels<MatchStatsViewModel>()
    private val args by navArgs<MatchStatsFragmentArgs>()

    private var _binding: ActivityMatchStatsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ActivityMatchStatsBinding.bind(view)

        // Change the color of the progress bar
        binding.progressBar.indeterminateDrawable.setColorFilter(
            Color.WHITE,
            PorterDuff.Mode.MULTIPLY
        )

        val id = args.matchId
        binding.toolbar.title = "${getString(R.string.match)} $id"

        viewModel.initialFetch(id)

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> {
                    binding.btnRefresh.isEnabled = false
                    binding.btnRefresh.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                }
                else -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.btnRefresh.visibility = View.VISIBLE
                    binding.btnRefresh.isEnabled = true
                }
            }
        }

        viewModel.isBookmarked.observe(viewLifecycleOwner) {
            if (it != 0L) {
                binding.imgBookmark.setImageResource(R.drawable.ic_match_note_bookmark_active)
            } else {
                binding.imgBookmark.setImageResource(R.drawable.ic_match_note_bookmark_inactive)
            }
        }

        binding.imgBookmark.setOnClickListener {
            if (viewModel.isBookmarked.value != 0L) {
                viewModel.removeFromBookmark(id)
            } else {
                viewModel.addToBookmark(id, onSuccess = {
                    toast("Bookmarked!")
                })
            }
        }

        binding.btnRefresh.setOnClickListener { viewModel.fetchMatchStats(id) }

        binding.viewPager.adapter = MatchStatsPagerAdapter(requireContext(), childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}