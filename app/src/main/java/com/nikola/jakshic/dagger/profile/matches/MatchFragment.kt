package com.nikola.jakshic.dagger.profile.matches

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.databinding.FragmentMatchBinding
import com.nikola.jakshic.dagger.matchstats.MatchStatsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchFragment : Fragment(R.layout.fragment_match) {
    private val viewModel by viewModels<MatchViewModel>()
    private var snackbar: Snackbar? = null

    private var _binding: FragmentMatchBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val EXTRA_ACCOUNT_ID = "account-id"

        fun newInstance(accountId: Long): MatchFragment {
            return MatchFragment().apply {
                arguments = bundleOf(EXTRA_ACCOUNT_ID to accountId)
            }
        }

        fun getAccountId(savedStateHandle: SavedStateHandle): Long {
            if (!savedStateHandle.contains(EXTRA_ACCOUNT_ID)) {
                throw IllegalArgumentException("""Required argument "$EXTRA_ACCOUNT_ID" is missing.""")
            }
            return savedStateHandle[EXTRA_ACCOUNT_ID]!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMatchBinding.bind(view)

        val adapter = MatchAdapter(isMatchesByHero = false) {
            findNavController().navigate(MatchStatsFragmentDirections.matchStatsAction(matchId = it))
        }

        binding.recView.layoutManager = LinearLayoutManager(context)
        binding.recView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL,
            ),
        )
        binding.recView.adapter = adapter
        binding.recView.setHasFixedSize(true)

        viewModel.list.observe(viewLifecycleOwner, Observer(adapter::submitList))
        viewModel.refreshStatus.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> binding.swipeRefresh.isRefreshing = true
                else -> binding.swipeRefresh.isRefreshing = false
            }
        }

        viewModel.loadMoreStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                Status.LOADING -> {
                    binding.swipeRefresh.isRefreshing = true
                    snackbar?.dismiss()
                }
                Status.SUCCESS -> {
                    binding.swipeRefresh.isRefreshing = false
                    snackbar?.dismiss()
                }
                else -> {
                    binding.swipeRefresh.isRefreshing = false
                    snackbar = Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.error_network),
                        Snackbar.LENGTH_INDEFINITE,
                    )
                    snackbar?.setActionTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.white,
                        ),
                    )
                    snackbar?.setAction(getString(R.string.retry)) {
                        viewModel.retry()
                    }
                    snackbar?.show()
                }
            }
        }
        binding.swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection()) {
                viewModel.fetchMatches()
            } else {
                toast(getString(R.string.error_network_connection))
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        snackbar?.dismiss()
        snackbar = null
    }
}
