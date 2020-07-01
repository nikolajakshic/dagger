package com.nikola.jakshic.dagger.leaderboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nikola.jakshic.dagger.HomeActivity
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.common.inflate
import com.nikola.jakshic.dagger.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_leaderboard.*

class LeaderboardFragment : Fragment(), HomeActivity.OnNavigationItemReselectedListener {

    private lateinit var adapter: RegionPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.fragment_leaderboard)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.inflateMenu(R.menu.menu_home)

        adapter = RegionPagerAdapter(requireContext(), childFragmentManager)

        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_home_search -> {
                    startActivity(Intent(activity, SearchActivity::class.java))
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