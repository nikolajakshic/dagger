package com.nikola.jakshic.dagger.competitive

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
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

@AndroidEntryPoint
class CompetitiveFragment : Fragment(R.layout.fragment_competitive),
    HomeFragment.OnNavigationItemReselectedListener {
    private val viewModel by viewModels<CompetitiveViewModel>()

    private var _binding: FragmentCompetitiveBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCompetitiveBinding.bind(view)

        binding.toolbar.inflateMenu(R.menu.menu_home)

        val adapter = CompetitiveAdapter {
            findNavController().navigate(MatchStatsFragmentDirections.matchStatsAction(matchId = it))
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.list.collectLatest { adapter.submitList(it) }
        }

        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.isLoading.collectLatest { binding.swipeRefresh.isRefreshing = it }
        }

        binding.swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection())
                viewModel.fetchCompetitiveMatches()
            else {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemReselected() {
        binding.recyclerView.smoothScrollToPosition(0)
    }
}