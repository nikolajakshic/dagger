package com.nikola.jakshic.dagger.matchstats

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.DaggerViewModelFactory
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.toast
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

        val id = intent.getLongExtra("match_id", -1)
        toolbar.title = "${getString(R.string.match)} $id"

        val viewModel = ViewModelProviders.of(this, factory)[MatchStatsViewModel::class.java]

        viewModel.initialFetch(id)

        viewModel.status.observe(this) {
            when (it) {
                Status.LOADING -> {
                    btnRefresh.isEnabled = false
                    btnRefresh.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                }
                else -> {
                    progressBar.visibility = View.INVISIBLE
                    btnRefresh.visibility = View.VISIBLE
                    btnRefresh.isEnabled = true
                }
            }
        }

        viewModel.isBookmarked.observe(this) {
            if (it != 0L) {
                imgBookmark.setImageResource(R.drawable.ic_match_note_bookmark_active)
            } else {
                imgBookmark.setImageResource(R.drawable.ic_match_note_bookmark_inactive)
            }
        }

        imgBookmark.setOnClickListener {
            if (viewModel.isBookmarked.value != 0L) {
                viewModel.removeFromBookmark(id)
            } else {
                viewModel.addToBookmark(id, onSuccess = {
                    toast("Bookmarked!")
                })
            }
        }

        btnRefresh.setOnClickListener { viewModel.fetchMatchStats(id) }

        viewPager.adapter = MatchStatsPagerAdapter(this, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }
}