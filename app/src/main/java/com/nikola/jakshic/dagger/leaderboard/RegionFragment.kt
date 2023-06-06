package com.nikola.jakshic.dagger.leaderboard

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.databinding.FragmentRegionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegionFragment : Fragment(R.layout.fragment_region) {
    companion object {
        private const val EXTRA_REGION = "region"

        fun newInstance(region: Region): RegionFragment {
            return RegionFragment().apply {
                arguments = bundleOf(EXTRA_REGION to region.name)
            }
        }

        fun getRegion(bundle: Bundle): Region {
            if (!bundle.containsKey(EXTRA_REGION)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_REGION" is missing.""")
            }
            return Region.valueOf(bundle.getString(EXTRA_REGION)!!)
        }

        fun getRegion(savedStateHandle: SavedStateHandle): Region {
            if (!savedStateHandle.contains(EXTRA_REGION)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_REGION" is missing.""")
            }
            return Region.valueOf(savedStateHandle[EXTRA_REGION]!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRegionBinding.bind(view)
        val viewModel = ViewModelProvider(this)[RegionViewModel::class.java]

        val region = getRegion(requireArguments())

        val adapter = LeaderboardAdapter()
        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
        binding.recView.setHasFixedSize(true)
        binding.recView.adapter = adapter

        val key = when (region) {
            Region.EUROPE -> LeaderboardFragment.Key.EUROPE
            Region.AMERICAS -> LeaderboardFragment.Key.AMERICA
            Region.CHINA -> LeaderboardFragment.Key.CHINA
            Region.SE_ASIA -> LeaderboardFragment.Key.SEA
        }
        LeaderboardFragment.setOnReselectListener(
            parentFragmentManager,
            viewLifecycleOwner,
            key,
        ) {
            binding.recView.smoothScrollToPosition(0)
        }
        binding.swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection()) {
                viewModel.fetchLeaderboard()
            } else {
                toast(getString(R.string.error_network_connection))
                binding.swipeRefresh.isRefreshing = false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.list.collectLatest(adapter::submitList)
                }
                launch {
                    viewModel.isLoading.collectLatest(binding.swipeRefresh::setRefreshing)
                }
            }
        }
    }
}
