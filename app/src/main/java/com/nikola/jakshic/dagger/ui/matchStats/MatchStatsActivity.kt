package com.nikola.jakshic.dagger.ui.matchstats

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.ui.DaggerViewModelFactory
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.ui.matchstats.overview.OverviewFragment
import kotlinx.android.synthetic.main.activity_match_stats.*
import javax.inject.Inject

class MatchStatsActivity : AppCompatActivity() {

    @Inject lateinit var factory: DaggerViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as DaggerApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_stats)

        // Change the color of the progress bar
        progressBar.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, OverviewFragment(), "overview-tag")
                    .commit()
        }

        val id = intent.getLongExtra("match_id", -1)
        toolbar.title = "Match $id"

        val viewModel = ViewModelProviders.of(this, factory)[MatchStatsViewModel::class.java]

        viewModel.initialFetch(id)

        viewModel.status.observe(this, Observer {
            when (it) {
                Status.LOADING -> {
                    btnRefresh.isEnabled = false
                    btnRefresh.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                else -> {
                    progressBar.visibility = View.GONE
                    btnRefresh.visibility = View.VISIBLE
                    btnRefresh.isEnabled = true
                }
            }
        })
        btnRefresh.setOnClickListener { viewModel.fetchMatchStats(id) }
    }
}