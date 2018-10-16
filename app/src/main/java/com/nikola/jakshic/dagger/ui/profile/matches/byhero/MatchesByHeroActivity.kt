package com.nikola.jakshic.dagger.ui.profile.matches.byhero

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.ui.DaggerViewModelFactory
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.ui.matchstats.MatchStatsActivity
import com.nikola.jakshic.dagger.ui.profile.matches.MatchAdapter
import kotlinx.android.synthetic.main.activity_matches_per_hero.*
import javax.inject.Inject

class MatchesByHeroActivity : AppCompatActivity() {

    @Inject lateinit var factory: DaggerViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as DaggerApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matches_per_hero)

        val accountId = intent.getLongExtra("account_id", -1)
        val heroId = intent.getIntExtra("hero_id", -1)

        val viewModel = ViewModelProviders.of(this, factory)[MatchesByHeroViewModel::class.java]

        viewModel.initialFetch(accountId, heroId)

        val adapter = MatchAdapter {
            val intent = Intent(this, MatchStatsActivity::class.java).apply {
                putExtra("match_id", it)
            }
            startActivity(intent)
        }

        recView.layoutManager = LinearLayoutManager(this)
        recView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recView.adapter = adapter
        recView.setHasFixedSize(true)

        viewModel.matches.observe(this, Observer {
            // If we submit empty or null list, previous data will be deleted,
            // and there will be nothing to show to the user
            if (it != null && it.size > 0) adapter.submitList(it)
        })

        var snackBar: Snackbar? = null

        viewModel.status.observe(this, Observer { status ->
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
                    snackBar?.setActionTextColor(ContextCompat.getColor(this@MatchesByHeroActivity, android.R.color.white))
                    snackBar?.setAction(getString(R.string.retry)) {
                        viewModel.retry()
                    }
                    snackBar?.show()
                }
            }
        })

        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }
}