package com.nikola.jakshic.dagger.leaderboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.search.SearchFragmentDirections
import kotlinx.android.synthetic.main.fragment_leaderboard.*

class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard), HomeFragment.OnNavigationItemReselectedListener {
    private var adapter: RegionPagerAdapter? = null

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
                    findNavController().navigate(SearchFragmentDirections.searchAction())
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    override fun onItemReselected() {
        val adapter = adapter ?: return // make it non-null

        val position = viewPager.currentItem
        // If the fragment is already instantiated, it returns that instance,
        // otherwise creates the new one
        val currentFragment = (adapter.instantiateItem(viewPager, position) as RegionFragment)

        currentFragment.onItemReselected()
    }
}