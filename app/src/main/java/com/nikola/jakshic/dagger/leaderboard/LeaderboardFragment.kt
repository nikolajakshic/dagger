package com.nikola.jakshic.dagger.leaderboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.databinding.FragmentLeaderboardBinding
import com.nikola.jakshic.dagger.search.SearchFragmentDirections

class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard),
    HomeFragment.OnNavigationItemReselectedListener {
    private var _binding: FragmentLeaderboardBinding? = null
    private val binding get() = _binding!!

    private var adapter: RegionPagerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLeaderboardBinding.bind(view)

        binding.toolbar.inflateMenu(R.menu.menu_home)

        adapter = RegionPagerAdapter(requireContext(), childFragmentManager)

        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 3
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.toolbar.setOnMenuItemClickListener {
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
        _binding = null
        adapter = null
    }

    override fun onItemReselected() {
        val adapter = adapter ?: return // make it non-null

        val position = binding.viewPager.currentItem
        // If the fragment is already instantiated, it returns that instance,
        // otherwise creates the new one
        val currentFragment =
            (adapter.instantiateItem(binding.viewPager, position) as RegionFragment)

        currentFragment.onItemReselected()
    }
}