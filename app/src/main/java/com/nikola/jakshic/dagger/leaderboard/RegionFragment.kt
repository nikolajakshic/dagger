package com.nikola.jakshic.dagger.leaderboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.databinding.FragmentRegionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_region.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val EXTRA_REGION = "region"

@AndroidEntryPoint
class RegionFragment : Fragment(R.layout.fragment_region),
    HomeFragment.OnNavigationItemReselectedListener {
    private val viewModel by viewModels<RegionViewModel>()

    private var _binding: FragmentRegionBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(region: Region): RegionFragment {
            val fragment = RegionFragment()
            val args = Bundle()
            args.putString(EXTRA_REGION, region.name.lowercase())
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegionBinding.bind(view)

        val region = requireArguments().getString(EXTRA_REGION)

        viewModel.initialFetch(region!!)

        val adapter = LeaderboardAdapter()
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.layoutManager = LinearLayoutManager(context)
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection())
                viewModel.fetchLeaderboard(region)
            else {
                toast(getString(R.string.error_network_connection))
                swipeRefresh.isRefreshing = false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.list.collectLatest(adapter::setData)
                }
                launch {
                    viewModel.isLoading.collectLatest(binding.swipeRefresh::setRefreshing)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemReselected() {
        recView.smoothScrollToPosition(0)
    }
}