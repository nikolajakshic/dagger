package com.nikola.jakshic.dagger.leaderboard

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RegionPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {
    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RegionFragment.newInstance(Region.EUROPE)
            1 -> RegionFragment.newInstance(Region.AMERICAS)
            2 -> RegionFragment.newInstance(Region.CHINA)
            else -> RegionFragment.newInstance(Region.SE_ASIA)
        }
    }
}
