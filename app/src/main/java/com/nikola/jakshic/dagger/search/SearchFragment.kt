package com.nikola.jakshic.dagger.search

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.bookmark.player.PlayerAdapter
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_search.*

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.activity_search) {
    private val viewModel by viewModels<SearchViewModel>()

    private val STATE_QUERY = "query"
    private val STATE_FOCUS = "focus"
    private val STATE_INITIAL = "initial"

    private var hasFocus = true
    private var query: String = ""
    private var initialState = true
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasFocus = savedInstanceState?.getBoolean(STATE_FOCUS) ?: true
        query = savedInstanceState?.getString(STATE_QUERY) ?: ""
        initialState = savedInstanceState?.getBoolean(STATE_INITIAL) ?: true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (initialState) {
            viewModel.getAllQueries()
            initialState = false
        }

        setupToolbar()

        val playerAdapter = PlayerAdapter {
            findNavController().navigate(SearchFragmentDirections.profileAction(accountId = it.id))
        }

        val historyAdapter = HistoryAdapter {
            searchView!!.setQuery(it, true)
        }

        recViewPlayers.layoutManager = LinearLayoutManager(requireContext())
        recViewPlayers.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recViewPlayers.adapter = playerAdapter
        recViewPlayers.setHasFixedSize(true)

        recViewHistory.layoutManager = LinearLayoutManager(requireContext())
        recViewHistory.adapter = historyAdapter
        recViewHistory.setHasFixedSize(true)

        viewModel.playerList.observe(viewLifecycleOwner, Observer(playerAdapter::setData))
        viewModel.historyList.observe(viewLifecycleOwner) {
            searchHistoryContainer.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
            if (it?.size ?: 0 != 0) {
                tvClearAll.visibility = View.VISIBLE
            } else {
                tvClearAll.visibility = View.INVISIBLE
            }
            historyAdapter.addData(it)
        }
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                Status.LOADING -> progressBar.visibility = View.VISIBLE
                else -> progressBar.visibility = View.GONE
            }
        }

        tvClearAll.setOnClickListener {
            viewModel.deleteSearchHistory()
            viewModel.getAllQueries()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView = null
    }

    private fun setupToolbar() {
        toolbar.inflateMenu(R.menu.menu_search)
        val searchItem = toolbar.menu.findItem(R.id.menu_search_search)
        searchItem.expandActionView()
        searchView = searchItem.actionView as SearchView
        val searchView = searchView ?: return // make it non-null

        searchView.queryHint = getString(R.string.search_players)
        searchView.setQuery(query, false)
        // Fix for landscape mode, editText is not set to match_parent
        searchView.maxWidth = Int.MAX_VALUE

        if (hasFocus) searchView.requestFocus() else searchView.clearFocus()

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?) = true

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                // SearchView.clearFocus() triggers QueryTextFocusChangeListener which toggles
                // search-history visibility, that doesn't look nice when we want to quit SearchFragment,
                // so we remove listener to prevent that.
                searchView.setOnQueryTextFocusChangeListener(null)
                searchView.clearFocus()
                findNavController().navigateUp()
                return false
            }
        })

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            this.hasFocus = hasFocus
            searchHistoryContainer.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.clearList()

                if (hasNetworkConnection())
                    viewModel.fetchPlayers(query)
                else
                    toast(getString(R.string.error_network_connection))

                viewModel.saveQuery(query)
                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query = newText
                viewModel.getQueries(query)
                return true
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_INITIAL, initialState)
        outState.putString(STATE_QUERY, query)
        outState.putBoolean(STATE_FOCUS, hasFocus)
    }
}