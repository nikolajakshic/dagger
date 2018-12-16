package com.nikola.jakshic.dagger.ui.matchstats

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.ui.matchstats.comparison.ComparisonFragment
import com.nikola.jakshic.dagger.ui.matchstats.overview.OverviewFragment

class MatchStatsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) =
            when (position) {
                0 -> OverviewFragment()
                else -> ComparisonFragment()
            }

    override fun getPageTitle(position: Int) =
            when (position) {
                0 -> context.getString(R.string.match_stats_overview)
                else -> context.getString(R.string.match_stats_comparison)
            }

    override fun getCount() = 2
}