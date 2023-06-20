package com.nikola.jakshic.dagger.competitive

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.databinding.FragmentCompetitiveBinding
import com.nikola.jakshic.dagger.matchstats.MatchStatsFragmentDirections
import com.nikola.jakshic.dagger.search.SearchFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompetitiveFragment : Fragment(R.layout.fragment_competitive) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCompetitiveBinding.bind(view)
        val viewModel = ViewModelProvider(this)[CompetitiveViewModel::class.java]

        binding.toolbar.inflateMenu(R.menu.menu_home)

        val adapter = CompetitiveAdapter {
            findNavController().navigate(MatchStatsFragmentDirections.matchStatsAction(matchId = it))
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        HomeFragment.setOnReselectListener(
            parentFragmentManager,
            viewLifecycleOwner,
            HomeFragment.Key.COMPETITIVE,
        ) {
            binding.recyclerView.smoothScrollToPosition(0)
        }
        binding.swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection()) {
                viewModel.fetchCompetitiveMatches()
            } else {
                toast(getString(R.string.error_network_connection))
                binding.swipeRefresh.isRefreshing = false
            }
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_home_search -> {
                    findNavController().navigate(SearchFragmentDirections.searchAction())
                    true
                }
                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.data.collectLatest(adapter::submitData)
                }
                launch {
                    viewModel.isLoading.collectLatest(binding.swipeRefresh::setRefreshing)
                }
            }
        }
    }
}
