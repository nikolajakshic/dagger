package com.nikola.jakshic.dagger.leaderboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.DaggerViewModelFactory
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.common.toast
import kotlinx.android.synthetic.main.fragment_region.*
import javax.inject.Inject

class RegionFragment : Fragment(), HomeFragment.OnNavigationItemReselectedListener {

    @Inject lateinit var factory: DaggerViewModelFactory

    companion object {
        fun newInstance(region: Region): RegionFragment {
            val fragment = RegionFragment()
            val args = Bundle()
            args.putString("region", region.name.toLowerCase())
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        (activity?.application as? DaggerApp)?.appComponent?.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_region)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val region = arguments?.getString("region")

        val viewModel = ViewModelProviders.of(this, factory)[RegionViewModel::class.java]

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