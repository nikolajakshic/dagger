package com.nikola.jakshic.dagger.matchstats

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nikola.jakshic.dagger.matchstats.comparison.ComparisonFragment
import com.nikola.jakshic.dagger.matchstats.overview.OverviewFragment

class MatchStatsPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment()
            1 -> ComparisonFragment()
            else -> throw IllegalStateException("Found more than 2 items.")
        }
    }
}
