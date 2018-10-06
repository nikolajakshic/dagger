package com.nikola.jakshic.dagger.ui.leaderboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.inflate
import com.nikola.jakshic.dagger.ui.HomeActivity
import com.nikola.jakshic.dagger.ui.search.SearchActivity
import com.nikola.jakshic.dagger.ui.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_leaderboard.*

class LeaderboardFragment : Fragment(), HomeActivity.OnNavigationItemReselectedListener {

    private lateinit var adapter: RegionPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_leaderboard)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.inflateMenu(R.menu.menu_home)

        adapter = RegionPagerAdapter(childFragmentManager)

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_home_search -> {
                    startActivity(Intent(activity, SearchActivity::class.java))
                    true
                }
                R.id.menu_home_settings -> {
                    startActivity(Intent(activity, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    override fun onItemReselected() {
        val position = viewPager.currentItem
        // If the fragment is already instantiated, it returns that instance,
        // otherwise creates the new one
        val currentFragment = (adapter.instantiateItem(viewPager, position) as RegionFragment)

        currentFragment.onItemReselected()
    }
}