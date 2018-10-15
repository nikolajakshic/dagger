package com.nikola.jakshic.dagger.ui.profile.matches

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.*
import com.nikola.jakshic.dagger.ui.DaggerViewModelFactory
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.ui.matchstats.MatchStatsActivity
import kotlinx.android.synthetic.main.fragment_match.*
import javax.inject.Inject

class MatchFragment : Fragment() {

    @Inject lateinit var factory: DaggerViewModelFactory

    override fun onAttach(context: Context?) {
        (activity?.application as DaggerApp).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_match)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this, factory)[MatchViewModel::class.java]

        val id = activity?.intent?.getLongExtra("account_id", -1) ?: -1

        viewModel.initialFetch(id)

        val adapter = MatchAdapter {
            val intent = Intent(context, MatchStatsActivity::class.java)
            intent.putExtra("match_id", it)
            startActivity(intent)
        }

        recView.layoutManager = LinearLayoutManager(context)
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.list.observe(this, Observer(adapter::submitList))
        viewModel.status.observe(this, Observer {
            when (it) {
                Status.LOADING -> swipeRefresh.isRefreshing = true
                else -> swipeRefresh.isRefreshing = false
            }
        })
        swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection())
                viewModel.fetchMatches(id)
            else {
                toast(getString(R.string.error_network_connection))
                swipeRefresh.isRefreshing = false
            }
        }
    }
}