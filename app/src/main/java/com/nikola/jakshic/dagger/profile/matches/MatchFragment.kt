package com.nikola.jakshic.dagger.profile.matches

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.databinding.FragmentMatchBinding
import com.nikola.jakshic.dagger.matchstats.MatchStatsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MatchFragment : Fragment(R.layout.fragment_match) {
    private var snackbar: Snackbar? = null

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
        val binding = FragmentMatchBinding.bind(view)

        val viewModel = ViewModelProvider(this)[MatchViewModel::class.java]

        val adapter = MatchAdapter {
            findNavController().navigate(MatchStatsFragmentDirections.matchStatsAction(matchId = it))
        }

        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL),
        )
        binding.recView.setHasFixedSize(false)
        binding.recView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collectLatest {
                    it.refresh as? LoadState.Error
                        ?: it.prepend as? LoadState.Error
                        ?: it.append as? LoadState.Error
                        ?: return@collectLatest
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
                        adapter.retry()
                    }
                    snackbar?.show()
                }
            }
        }
        binding.swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection()) {
                adapter.refresh()
            } else {
                toast(getString(R.string.error_network_connection))
                binding.swipeRefresh.isRefreshing = false
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow
                    .debounce {
                        val loading = it.refresh is LoadState.Loading ||
                            it.prepend is LoadState.Loading ||
                            it.append is LoadState.Loading
                        if (loading) 0 else 300
                    }
                    .collectLatest {
                        binding.swipeRefresh.isRefreshing = it.refresh is LoadState.Loading ||
                            it.prepend is LoadState.Loading ||
                            it.append is LoadState.Loading
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbar?.dismiss()
        snackbar = null
    }
}
