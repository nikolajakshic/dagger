package com.nikola.jakshic.dagger.leaderboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_region.*

@AndroidEntryPoint
class RegionFragment : Fragment(R.layout.fragment_region), HomeFragment.OnNavigationItemReselectedListener {
    private val viewModel by viewModels<RegionViewModel>()

    companion object {
        fun newInstance(region: Region): RegionFragment {
            val fragment = RegionFragment()
            val args = Bundle()
            args.putString("region", region.name.toLowerCase())
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val region = arguments?.getString("region")

        viewModel.initialFetch(region!!)

        val adapter = LeaderboardAdapter()
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.layoutManager = LinearLayoutManager(context)
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.list.observe(viewLifecycleOwner, Observer(adapter::addData))

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> swipeRefresh.isRefreshing = true
                else -> swipeRefresh.isRefreshing = false
            }
        }
        swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection())
                viewModel.fetchLeaderboard(region)
            else {
                toast(getString(R.string.error_network_connection))
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onItemReselected() {
        recView.smoothScrollToPosition(0)
    }
}