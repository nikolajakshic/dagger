package com.nikola.jakshic.dagger.profile.matches.byhero

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.databinding.FragmentMatchesByHeroBinding
import com.nikola.jakshic.dagger.matchstats.MatchStatsFragmentDirections
import com.nikola.jakshic.dagger.profile.matches.MatchAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchesByHeroFragment : Fragment(R.layout.fragment_matches_by_hero) {
    private val viewModel by viewModels<MatchesByHeroViewModel>()

    private var snackBar: Snackbar? = null

    private var _binding: FragmentMatchesByHeroBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMatchesByHeroBinding.bind(view)

        val adapter = MatchAdapter(isMatchesByHero = true) {
            findNavController().navigate(MatchStatsFragmentDirections.matchStatsAction(matchId = it))
        }

        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL,
            ),
        )
        binding.recView.adapter = adapter
        binding.recView.setHasFixedSize(true)

        viewModel.matches.observe(viewLifecycleOwner) {
            // If we submit empty or null list, previous data will be deleted,
            // and there will be nothing to show to the user
            if (it != null && it.size > 0) adapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.heroImage.collectLatest {
                    adapter.heroImage = it
                }
            }
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                Status.LOADING -> {
                    binding.swipeRefresh.isRefreshing = true
                    snackBar?.dismiss()
                }
                Status.SUCCESS -> {
                    binding.swipeRefresh.isRefreshing = false
                    snackBar?.dismiss()
                }
                else -> {
                    binding.swipeRefresh.isRefreshing = false
                    snackBar = Snackbar.make(
                        binding.swipeRefresh,
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
                        viewModel.retry()
                    }
                    snackBar?.show()
                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        snackBar?.dismiss()
        snackBar = null
    }
}
