package com.nikola.jakshic.dagger.profile.matches.byhero

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.DaggerViewModelFactory
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.matchstats.MatchStatsFragmentDirections
import com.nikola.jakshic.dagger.profile.matches.MatchAdapter
import kotlinx.android.synthetic.main.activity_matches_per_hero.*
import javax.inject.Inject

class MatchesByHeroFragment : Fragment(R.layout.activity_matches_per_hero) {
    private val args by navArgs<MatchesByHeroFragmentArgs>()

    @Inject lateinit var factory: DaggerViewModelFactory

    override fun onAttach(context: Context) {
        (requireActivity().application as DaggerApp).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val accountId = args.accountId
        val heroId = args.heroId

        val viewModel = ViewModelProviders.of(this, factory)[MatchesByHeroViewModel::class.java]

        viewModel.initialFetch(accountId, heroId)

        val adapter = MatchAdapter {
            findNavController().navigate(MatchStatsFragmentDirections.matchStatsAction(matchId = it))
        }

        recView.layoutManager = LinearLayoutManager(requireContext())
        recView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.matches.observe(viewLifecycleOwner) {
            // If we submit empty or null list, previous data will be deleted,
            // and there will be nothing to show to the user
            if (it != null && it.size > 0) adapter.submitList(it)
        }

        var snackBar: Snackbar? = null

        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                Status.LOADING -> {
                    swipeRefresh.isRefreshing = true
                    snackBar?.dismiss()
                }
                Status.SUCCESS -> {
                    swipeRefresh.isRefreshing = false
                    snackBar?.dismiss()
                }
                else -> {
                    swipeRefresh.isRefreshing = false
                    snackBar = Snackbar.make(swipeRefresh, getString(R.string.error_network), Snackbar.LENGTH_INDEFINITE)
                    snackBar?.setActionTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                    snackBar?.setAction(getString(R.string.retry)) {
                        viewModel.retry()
                    }
                    snackBar?.show()
                }
            }
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }
}