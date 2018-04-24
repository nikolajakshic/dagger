package com.nikola.jakshic.dagger.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.Status
import com.nikola.jakshic.dagger.model.SearchHistory
import com.nikola.jakshic.dagger.toast
import com.nikola.jakshic.dagger.util.NetworkUtil
import com.nikola.jakshic.dagger.view.adapter.HistoryAdapter
import com.nikola.jakshic.dagger.view.adapter.PlayerAdapter
import com.nikola.jakshic.dagger.viewModel.DaggerViewModelFactory
import com.nikola.jakshic.dagger.viewModel.SearchViewModel
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject lateinit var factory: DaggerViewModelFactory
    private lateinit var viewModel: SearchViewModel
    private var hasFocus = true
    private var query: String? = null
    private lateinit var searchView: SearchView
    private val STATE_QUERY = "query"
    private val STATE_FOCUS = "focus"

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as DaggerApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProviders.of(this, factory)[SearchViewModel::class.java]

        if (savedInstanceState != null) {
            hasFocus = savedInstanceState.getBoolean(STATE_FOCUS)
            query = savedInstanceState.getString(STATE_QUERY)
        } else {
            // Show search history when activity starts
            viewModel.getAllQueries()
        }

        val playerAdapter = PlayerAdapter(this)
        val historyAdapter = HistoryAdapter {
            searchView.setQuery(it, true)
        }

        recViewPlayers.layoutManager = LinearLayoutManager(this)
        recViewPlayers.adapter = playerAdapter
        recViewPlayers.setHasFixedSize(true)

        recViewHistory.layoutManager = LinearLayoutManager(this)
        recViewHistory.adapter = historyAdapter
        recViewHistory.setHasFixedSize(true)

        viewModel.playerList.observe(this, Observer(playerAdapter::addData))
        viewModel.historyList.observe(this, Observer {
            recViewHistory.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
            historyAdapter.addData(it)
        })
        viewModel.status.observe(this, Observer {
            when (it) {
                Status.LOADING -> progressBar.visibility = View.VISIBLE
                else -> progressBar.visibility = View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu?.findItem(R.id.menu_search_search)
        searchItem?.expandActionView()
        searchView = searchItem?.actionView as SearchView

        searchView.queryHint = "Search Players"
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
            recViewHistory.visibility = if (hasFocus) View.VISIBLE else View.INVISIBLE
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.clearList()

                if (NetworkUtil.isActive(this@SearchActivity))
                    viewModel.fetchPlayers(query!!)
                else
                    toast("Check network connection!")

                viewModel.saveQuery(SearchHistory(query))
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

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(STATE_QUERY, searchView.query.toString())
        outState?.putBoolean(STATE_FOCUS, searchView.hasFocus())
        super.onSaveInstanceState(outState)
    }
}