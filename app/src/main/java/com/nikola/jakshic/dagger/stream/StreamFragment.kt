package com.nikola.jakshic.dagger.stream

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.databinding.FragmentStreamBinding
import com.nikola.jakshic.dagger.search.SearchFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StreamFragment :
    Fragment(R.layout.fragment_stream),
    HomeFragment.OnNavigationItemReselectedListener {
    private val viewModel by viewModels<StreamViewModel>()

    private var _binding: FragmentStreamBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStreamBinding.bind(view)
        binding.toolbar.inflateMenu(R.menu.menu_home)

        val adapter = StreamAdapter { userName ->
            startActivity(StreamPlayerActivity.createIntent(requireContext(), userName))
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        viewModel.initialFetch()

        binding.swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection()) {
                viewModel.getStreams()
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
                    viewModel.status.collectLatest {
                        if (it == Status.ERROR) {
                            binding.tvNetworkError.visibility = View.VISIBLE
                        } else {
                            binding.tvNetworkError.visibility = View.GONE
                        }
                    }
                }
                launch {
                    viewModel.streams.collectLatest { adapter.setData(it) }
                }
                launch {
                    viewModel.isLoading.collectLatest { binding.swipeRefresh.isRefreshing = it }
                }
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
