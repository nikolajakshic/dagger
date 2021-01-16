package com.nikola.jakshic.dagger.profile.matches.byhero

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.matchstats.MatchStatsFragmentDirections
import com.nikola.jakshic.dagger.profile.matches.MatchAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_matches_per_hero.*

@AndroidEntryPoint
class MatchesByHeroFragment : Fragment(R.layout.activity_matches_per_hero) {
    private val viewModel by viewModels<MatchesByHeroViewModel>()
    private val args by navArgs<MatchesByHeroFragmentArgs>()
    private var snackBar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val accountId = args.accountId
        val heroId = args.heroId

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

    override fun onDestroyView() {
        super.onDestroyView()
        snackBar?.dismiss()
        snackBar = null
    }
}