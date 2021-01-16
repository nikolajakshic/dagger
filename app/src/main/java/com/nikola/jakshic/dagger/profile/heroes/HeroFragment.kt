package com.nikola.jakshic.dagger.profile.heroes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.profile.ProfileFragmentArgs
import com.nikola.jakshic.dagger.profile.ProfileFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_hero.*

@AndroidEntryPoint
class HeroFragment : Fragment(R.layout.fragment_hero), HeroSortDialog.OnSortListener {
    private val viewModel by viewModels<HeroViewModel>()
    private var adapter: HeroAdapter? = null
    private var id: Long = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = ProfileFragmentArgs.fromBundle(requireParentFragment().requireArguments()).accountId

        viewModel.initialFetch(id)

        adapter = HeroAdapter {
            findNavController().navigate(
                ProfileFragmentDirections.matchesByHeroAction(
                    accountId = id, heroId = it
                )
            )
        }

        recView.layoutManager = LinearLayoutManager(context)
        recView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.list.observe(viewLifecycleOwner, Observer(adapter!!::addData))
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> swipeRefresh.isRefreshing = true
                else -> swipeRefresh.isRefreshing = false
            }
        }

        val sortDialog = HeroSortDialog()
        sortDialog.setTargetFragment(this, 301)

        btnSort.setOnClickListener {
            if (!sortDialog.isAdded) sortDialog.show(parentFragmentManager, null)
        }

        swipeRefresh.setOnRefreshListener {
            if (hasNetworkConnection())
                viewModel.fetchHeroes(id)
            else {
                toast(getString(R.string.error_network_connection))
                swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    override fun onSort(sort: Int) {
        val adapter = adapter ?: return // make it non-null

        // Remove previous observers b/c we are attaching new LiveData
        viewModel.list.removeObservers(viewLifecycleOwner)

        when (sort) {
            0 -> viewModel.sortByGames(id)
            1 -> viewModel.sortByWinRate(id)
            2 -> viewModel.sortByWins(id)
            3 -> viewModel.sortByLosses(id)
        }
        // Set to null first, to delete all the items otherwise the list wont be scrolled to the first item
        adapter.addData(null)
        // Attach the observer to the new LiveData
        viewModel.list.observe(viewLifecycleOwner, Observer(adapter::addData))
    }
}