package com.nikola.jakshic.dagger.matchstats

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_match_stats.*

@AndroidEntryPoint
class MatchStatsFragment : Fragment(R.layout.activity_match_stats) {
    private val viewModel by viewModels<MatchStatsViewModel>()
    private val args by navArgs<MatchStatsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Change the color of the progress bar
        progressBar.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)

        val id = args.matchId
        toolbar.title = "${getString(R.string.match)} $id"

        viewModel.initialFetch(id)

        viewModel.status.observe(viewLifecycleOwner) {
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

        viewModel.isBookmarked.observe(viewLifecycleOwner) {
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

        viewPager.adapter = MatchStatsPagerAdapter(requireContext(), childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }
}