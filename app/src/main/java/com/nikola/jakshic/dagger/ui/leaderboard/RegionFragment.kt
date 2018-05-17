package com.nikola.jakshic.dagger.ui.leaderboard

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikola.jakshic.dagger.*
import com.nikola.jakshic.dagger.R.id.recView
import com.nikola.jakshic.dagger.R.id.swipeRefresh
import com.nikola.jakshic.dagger.vo.Region
import com.nikola.jakshic.dagger.ui.DaggerViewModelFactory
import com.nikola.jakshic.dagger.ui.HomeActivity
import com.nikola.jakshic.dagger.ui.Status
import kotlinx.android.synthetic.main.fragment_region.*
import javax.inject.Inject

class RegionFragment : Fragment(), HomeActivity.OnNavigationItemReselectedListener {

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

    override fun onAttach(context: Context?) {
        (activity?.application as? DaggerApp)?.appComponent?.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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

        viewModel.list.observe(this, Observer(adapter::addData))

        viewModel.status.observe(this, Observer {
            when (it) {
                Status.LOADING -> swipeRefresh.isRefreshing = true
                else -> swipeRefresh.isRefreshing = false
            }
        })
        swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection())
                viewModel.fetchLeaderboard(region)
            else {
                toast("Check network connection!")
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onItemReselected() {
        recView.smoothScrollToPosition(0)
    }
}