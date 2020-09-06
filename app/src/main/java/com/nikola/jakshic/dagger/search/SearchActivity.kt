package com.nikola.jakshic.dagger.search

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.bookmark.player.PlayerAdapter
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.hasNetworkConnection
import com.nikola.jakshic.dagger.common.toast
import com.nikola.jakshic.dagger.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_search.*

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModels()
    private var hasFocus = true
    private var query: String? = null
    private lateinit var searchView: SearchView
    private val STATE_QUERY = "query"
    private val STATE_FOCUS = "focus"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        setContentView(R.layout.activity_search)

        if (savedInstanceState != null) {
            hasFocus = savedInstanceState.getBoolean(STATE_FOCUS)
            query = savedInstanceState.getString(STATE_QUERY)
        } else {
            // Show search history when activity starts
            viewModel.getAllQueries()
        }

        val playerAdapter = PlayerAdapter {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("account_id", it.id)
            startActivity(intent)
        }

        val historyAdapter = HistoryAdapter {
            searchView.setQuery(it, true)
        }

        recViewPlayers.layoutManager = LinearLayoutManager(this)
        recViewPlayers.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recViewPlayers.adapter = playerAdapter
        recViewPlayers.setHasFixedSize(true)

        recViewHistory.layoutManager = LinearLayoutManager(this)
        recViewHistory.adapter = historyAdapter
        recViewHistory.setHasFixedSize(true)

        viewModel.playerList.observe(this, Observer(playerAdapter::addData))
        viewModel.historyList.observe(this, Observer {
            searchHistoryContainer.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
            if (it?.size ?: 0 != 0) {
                tvClearAll.visibility = View.VISIBLE
            } else {
                tvClearAll.visibility = View.INVISIBLE
            }
            historyAdapter.addData(it)
        })
        viewModel.status.observe(this, Observer {
            when (it) {
                Status.LOADING -> progressBar.visibility = View.VISIBLE
                else -> progressBar.visibility = View.GONE
            }
        })

        tvClearAll.setOnClickListener {
            viewModel.deleteSearchHistory()
            viewModel.getAllQueries()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu?.findItem(R.id.menu_search_search)
        searchItem?.expandActionView()
        searchView = searchItem?.actionView as SearchView

        searchView.queryHint = getString(R.string.search_players)
        searchView.setQuery(query, false)
        // Fix for landscape mode, editText is not set to match_parent
        searchView.maxWidth = Int.MAX_VALUE

        if (hasFocus) searchView.requestFocus() else searchView.clearFocus()

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?) = true

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
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
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(STATE_QUERY, searchView.query.toString())
        outState.putBoolean(STATE_FOCUS, searchView.hasFocus())
        super.onSaveInstanceState(outState)
    }
}