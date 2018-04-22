package com.nikola.jakshic.dagger.view.fragment

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
import android.widget.Toast
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.Status
import com.nikola.jakshic.dagger.repository.Region
import com.nikola.jakshic.dagger.util.NetworkUtil
import com.nikola.jakshic.dagger.view.adapter.LeaderboardAdapter
import com.nikola.jakshic.dagger.viewModel.RegionViewModel
import com.nikola.jakshic.dagger.viewModel.DaggerViewModelFactory
import kotlinx.android.synthetic.main.fragment_region.*
import javax.inject.Inject

class RegionFragment : Fragment() {

    @Inject
    lateinit var factory: DaggerViewModelFactory

    companion object {
        @JvmStatic // necessary because we are calling this method from java
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
        return inflater.inflate(R.layout.fragment_region, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val region = arguments?.getString("region")

        val viewModel = ViewModelProviders.of(this, factory)[RegionViewModel::class.java]

        viewModel.initialFetch(region!!)

        val adapter = LeaderboardAdapter()
        recview_region.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recview_region.layoutManager = LinearLayoutManager(context)
        recview_region.adapter = adapter
        recview_region.setHasFixedSize(true)

        viewModel.list.observe(this, Observer(adapter::addData))

        viewModel.status.observe(this, Observer {
            when (it) {
                Status.LOADING -> swiperefresh_region.isRefreshing = true
                else -> swiperefresh_region.isRefreshing = false
            }
        })

        swiperefresh_region.setOnRefreshListener {
            if (NetworkUtil.isActive(context))
                viewModel.fetchLeaderboard(region)
            else {
                Toast.makeText(activity, "Check network connection!", Toast.LENGTH_SHORT).show()
                swiperefresh_region.isRefreshing = false
            }
        }
    }
}