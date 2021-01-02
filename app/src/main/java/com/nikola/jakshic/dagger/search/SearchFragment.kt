package com.nikola.jakshic.dagger.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.bookmark.player.PlayerAdapter
import com.nikola.jakshic.dagger.common.DaggerViewModelFactory
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchFragment : Fragment(R.layout.activity_search) {

    @Inject lateinit var factory: DaggerViewModelFactory
    private lateinit var viewModel: SearchViewModel
    private var hasFocus = true
    private var query: String? = null
    private lateinit var searchView: SearchView
    private val STATE_QUERY = "query"
    private val STATE_FOCUS = "focus"

    override fun onAttach(context: Context) {
        (requireActivity().application as DaggerApp).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, factory)[SearchViewModel::class.java]

        setupToolbar()

        if (savedInstanceState != null) {
            hasFocus = savedInstanceState.getBoolean(STATE_FOCUS)
            query = savedInstanceState.getString(STATE_QUERY)
        } else {
            // Show search history when activity starts
            viewModel.getAllQueries()
        }

        val playerAdapter = PlayerAdapter {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            intent.putExtra("account_id", it.id)
            startActivity(intent)
        }

        val historyAdapter = HistoryAdapter {
            searchView.setQuery(it, true)
        }

        recViewPlayers.layoutManager = LinearLayoutManager(requireContext())
        recViewPlayers.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recViewPlayers.adapter = playerAdapter
        recViewPlayers.setHasFixedSize(true)

        recViewHistory.layoutManager = LinearLayoutManager(requireContext())
        recViewHistory.adapter = historyAdapter
        recViewHistory.setHasFixedSize(true)

        viewModel.playerList.observe(viewLifecycleOwner, Observer(playerAdapter::addData))
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

    private fun setupToolbar() {
        toolbar.inflateMenu(R.menu.menu_search)
        val searchItem = toolbar.menu.findItem(R.id.menu_search_search)
        searchItem.expandActionView()
        searchView = searchItem.actionView as SearchView

        searchView.queryHint = getString(R.string.search_players)
        searchView.setQuery(query, false)
        // Fix for landscape mode, editText is not set to match_parent
        searchView.maxWidth = Int.MAX_VALUE

        if (hasFocus) searchView.requestFocus() else searchView.clearFocus()

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?) = true

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                findNavController().navigateUp()
                return false
            }
        })

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            this.hasFocus = hasFocus
            searchHistoryContainer.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.clearList()

                if (hasNetworkConnection())
                    viewModel.fetchPlayers(query!!)
                else
                    toast(getString(R.string.error_network_connection))

                viewModel.saveQuery(query!!)
                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.getQueries(newText!!)
                return true
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(STATE_QUERY, searchView.query.toString())
        outState.putBoolean(STATE_FOCUS, searchView.hasFocus())
        super.onSaveInstanceState(outState)
    }
}