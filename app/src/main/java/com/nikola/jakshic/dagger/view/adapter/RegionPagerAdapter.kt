package com.nikola.jakshic.dagger.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.nikola.jakshic.dagger.repository.Region
import com.nikola.jakshic.dagger.view.fragment.RegionFragment

class RegionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> RegionFragment.newInstance(Region.EUROPE)
                1 -> RegionFragment.newInstance(Region.AMERICAS)
                2 -> RegionFragment.newInstance(Region.CHINA)
                else -> RegionFragment.newInstance(Region.SE_ASIA)
            }

    override fun getPageTitle(position: Int): CharSequence? =
            when (position) {
                0 -> "Europe"
                1 -> "America"
                2 -> "China"
                else -> "SEA"
            }

    override fun getCount() = 4
}