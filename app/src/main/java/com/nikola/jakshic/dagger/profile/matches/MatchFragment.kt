package com.nikola.jakshic.dagger.profile.matches

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.DaggerViewModelFactory
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.matchstats.MatchStatsFragmentDirections
import com.nikola.jakshic.dagger.profile.ProfileFragmentArgs
import kotlinx.android.synthetic.main.fragment_match.*
import javax.inject.Inject

class MatchFragment : Fragment() {
    private var snackbar: Snackbar? = null
    @Inject lateinit var factory: DaggerViewModelFactory

    override fun onAttach(context: Context) {
        (activity?.application as DaggerApp).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_match)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this, factory)[MatchViewModel::class.java]

        val id = ProfileFragmentArgs.fromBundle(requireParentFragment().requireArguments()).accountId

        viewModel.initialFetch(id)

        val adapter = MatchAdapter {
            findNavController().navigate(MatchStatsFragmentDirections.matchStatsAction(matchId = it))
        }

        recView.layoutManager = LinearLayoutManager(context)
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.list.observe(viewLifecycleOwner, Observer(adapter::submitList))
        viewModel.refreshStatus.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> swipeRefresh.isRefreshing = true
                else -> swipeRefresh.isRefreshing = false
            }
        }

        viewModel.loadMoreStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                Status.LOADING -> {
                    swipeRefresh.isRefreshing = true
                    snackbar?.dismiss()
                }
                Status.SUCCESS -> {
                    swipeRefresh.isRefreshing = false
                    snackbar?.dismiss()
                }
                else -> {
                    swipeRefresh.isRefreshing = false
                    snackbar = Snackbar.make(swipeRefresh, getString(R.string.error_network), Snackbar.LENGTH_INDEFINITE)
                    snackbar?.setActionTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                    snackbar?.setAction(getString(R.string.retry)) {
                        viewModel.retry()
                    }
                    snackbar?.show()
                }
            }
        }
        swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection())
                viewModel.fetchMatches(id)
            else {
                toast(getString(R.string.error_network_connection))
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackbar?.dismiss()
        snackbar = null
    }
}