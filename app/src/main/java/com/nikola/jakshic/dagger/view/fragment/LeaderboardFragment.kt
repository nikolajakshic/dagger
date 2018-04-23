package com.nikola.jakshic.dagger.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.inflate
import com.nikola.jakshic.dagger.view.adapter.RegionPagerAdapter
import kotlinx.android.synthetic.main.fragment_leaderboard.*

class LeaderboardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_leaderboard)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RegionPagerAdapter(childFragmentManager)

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onResume() {
        super.onResume()
        // This fragment is an item from BottomNavigationView
        // Set the proper title when this fragment is not hidden
        if (!isHidden) activity?.title = "Leaderboard"
    }
}