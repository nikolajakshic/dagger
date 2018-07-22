package com.nikola.jakshic.dagger.ui.competitive

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikola.jakshic.dagger.*
import com.nikola.jakshic.dagger.ui.DaggerViewModelFactory
import com.nikola.jakshic.dagger.ui.HomeActivity
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.ui.matchstats.MatchStatsActivity
import com.nikola.jakshic.dagger.ui.search.SearchActivity
import com.nikola.jakshic.dagger.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_competitive.*
import javax.inject.Inject

class CompetitiveFragment : Fragment(), HomeActivity.OnNavigationItemReselectedListener {

    @Inject lateinit var factory: DaggerViewModelFactory

    override fun onAttach(context: Context?) {
        (activity?.application as DaggerApp).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_competitive)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.inflateMenu(R.menu.menu_home)

        val viewModel = ViewModelProviders.of(this, factory)[CompetitiveViewModel::class.java]

        val adapter = CompetitiveAdapter(context) {
            val intent = Intent(activity, MatchStatsActivity::class.java)
            intent.putExtra("match_id", it)
            startActivity(intent)
        }
        recView.layoutManager = LinearLayoutManager(context)
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.initialFetch()
        viewModel.list.observe(this, Observer(adapter::submitList))
        viewModel.status.observe(this, Observer {
            when (it) {
                Status.LOADING -> swipeRefresh.isRefreshing = true
                else -> swipeRefresh.isRefreshing = false
            }
        })
        swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection())
                viewModel.refreshData()
            else {
                toast("Check network connection!")
                swipeRefresh.isRefreshing = false
            }
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_home_search -> {
                    startActivity(Intent(activity, SearchActivity::class.java))
                    true
                }
                R.id.menu_home_settings -> {
                    startActivity(Intent(activity, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    override fun onItemReselected() {
        recView.smoothScrollToPosition(0)
    }
}