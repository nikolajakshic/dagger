package com.nikola.jakshic.dagger.profile.matches.byhero

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.databinding.FragmentMatchesByHeroBinding
import com.nikola.jakshic.dagger.matchstats.MatchStatsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchesByHeroFragment : Fragment(R.layout.fragment_matches_by_hero) {
    private var snackBar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMatchesByHeroBinding.bind(view)
        val viewModel = ViewModelProvider(this)[MatchesByHeroViewModel::class.java]

        val adapter = MatchesByHeroAdapter {
            findNavController().navigate(MatchStatsFragmentDirections.matchStatsAction(matchId = it))
        }
        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL,
            ),
        )
        binding.recView.setHasFixedSize(true)
        binding.recView.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collectLatest(adapter::submitData)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest {
                    it.source.refresh as? LoadState.Error
                        ?: it.source.prepend as? LoadState.Error
                        ?: it.source.append as? LoadState.Error
                        ?: return@collectLatest
                    snackBar = Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.error_network),
                        Snackbar.LENGTH_INDEFINITE,
                    )
                    snackBar?.setActionTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.white,
                        ),
                    )
                    snackBar?.setAction(getString(R.string.retry)) {
                        adapter.retry()
                    }
                    snackBar?.show()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest {
                    binding.swipeRefresh.isRefreshing = it.refresh is LoadState.Loading ||
                        it.prepend is LoadState.Loading ||
                        it.append is LoadState.Loading
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackBar?.dismiss()
        snackBar = null
    }
}
