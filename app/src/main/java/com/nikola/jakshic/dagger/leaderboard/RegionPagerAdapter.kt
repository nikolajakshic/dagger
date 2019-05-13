package com.nikola.jakshic.dagger.leaderboard

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nikola.jakshic.dagger.R

class RegionPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> RegionFragment.newInstance(Region.EUROPE)
            1 -> RegionFragment.newInstance(Region.AMERICAS)
            2 -> RegionFragment.newInstance(Region.CHINA)
            else -> RegionFragment.newInstance(Region.SE_ASIA)
        }

    override fun getPageTitle(position: Int): CharSequence? =
        when (position) {
            0 -> context.getString(R.string.region_europe)
            1 -> context.getString(R.string.region_america)
            2 -> context.getString(R.string.region_china)
            else -> context.getString(R.string.region_sea)
        }

    override fun getCount() = 4
}