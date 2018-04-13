package com.nikola.jakshic.dagger.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.view.adapter.RegionPagerAdapter
import kotlinx.android.synthetic.main.fragment_leaderboard.*

class LeaderboardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RegionPagerAdapter(childFragmentManager)

        pagerLeaderboard.adapter = adapter
        pagerLeaderboard.offscreenPageLimit = 3
        tabLeaderboard.setupWithViewPager(pagerLeaderboard)
    }

    override fun onResume() {
        super.onResume()
        if (!isHidden) activity?.title = "Leaderboard"
    }
}