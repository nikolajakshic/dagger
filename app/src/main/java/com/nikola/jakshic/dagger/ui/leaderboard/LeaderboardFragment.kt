package com.nikola.jakshic.dagger.ui.leaderboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.R.id.tabLayout
import com.nikola.jakshic.dagger.R.id.viewPager
import com.nikola.jakshic.dagger.inflate
import com.nikola.jakshic.dagger.ui.HomeActivity
import kotlinx.android.synthetic.main.fragment_leaderboard.*

class LeaderboardFragment : Fragment(), HomeActivity.OnNavigationItemReselectedListener {

    private lateinit var adapter: RegionPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_leaderboard)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RegionPagerAdapter(childFragmentManager)

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

    override fun onItemReselected() {
        val position = viewPager.currentItem
        // If the fragment is already instantiated, it returns that instance,
        // otherwise creates the new one
        val currentFragment = (adapter.instantiateItem(viewPager, position) as RegionFragment)

        currentFragment.onItemReselected()
    }
}