package com.nikola.jakshic.dagger.matchstats

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.databinding.FragmentMatchStatsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchStatsFragment : Fragment(R.layout.fragment_match_stats) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMatchStatsBinding.bind(view)
        val viewModel = ViewModelProvider(this)[MatchStatsViewModel::class.java]

        val id = MatchStatsFragmentArgs.fromBundle(requireArguments()).matchId
        binding.toolbar.title = "${getString(R.string.match)} $id"

        binding.viewPager.adapter = MatchStatsPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val title = when (position) {
                0 -> getString(R.string.match_stats_overview)
                1 -> getString(R.string.match_stats_comparison)
                else -> throw IllegalStateException("Found more than 2 tabs.")
            }
            tab.text = title
        }.attach()

        binding.imgBookmark.setOnClickListener {
            if (viewModel.isBookmarked.value != 0L) {
                viewModel.removeFromBookmark()
            } else {
                viewModel.addToBookmark()
            }
        }
        binding.btnRefresh.setOnClickListener { viewModel.fetchMatchStats() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isBookmarked.collectLatest {
                    if (it != 0L) {
                        binding.imgBookmark.setImageResource(R.drawable.ic_match_note_bookmark_active)
                    } else {
                        binding.imgBookmark.setImageResource(R.drawable.ic_match_note_bookmark_inactive)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.successfullyBookmarked.collectLatest {
                    toast(getString(R.string.bookmarked))
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collectLatest { isLoading ->
                    binding.btnRefresh.isEnabled = !isLoading
                    binding.btnRefresh.isInvisible = isLoading
                    binding.progressBar.isInvisible = !isLoading
                }
            }
        }
    }
}
