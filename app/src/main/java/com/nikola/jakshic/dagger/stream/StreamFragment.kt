package com.nikola.jakshic.dagger.stream

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
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
import com.nikola.jakshic.dagger.databinding.FragmentStreamBinding
import com.nikola.jakshic.dagger.search.SearchFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StreamFragment : Fragment(R.layout.fragment_stream) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentStreamBinding.bind(view)
        val viewModel = ViewModelProvider(this)[StreamViewModel::class.java]

        binding.toolbar.inflateMenu(R.menu.menu_home)

        val adapter = StreamAdapter { userName ->
            startActivity(StreamPlayerActivity.createIntent(requireContext(), userName))
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        HomeFragment.setOnReselectListener(
            parentFragmentManager,
            viewLifecycleOwner,
            HomeFragment.Key.STREAM,
        ) {
            binding.recyclerView.smoothScrollToPosition(0)
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
        binding.swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection()) {
                viewModel.fetchStreams()
            } else {
                toast(getString(R.string.error_network_connection))
                binding.swipeRefresh.isRefreshing = false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.streamUiState.collectLatest { streamUiState ->
                    adapter.submitList(streamUiState.streams)
                    binding.swipeRefresh.isRefreshing = streamUiState.isLoading
                    binding.tvNetworkError.isVisible =
                        streamUiState.error && streamUiState.streams.isEmpty()
                }
            }
        }
    }
}
