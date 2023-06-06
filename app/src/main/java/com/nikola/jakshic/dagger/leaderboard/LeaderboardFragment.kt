package com.nikola.jakshic.dagger.leaderboard

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.databinding.FragmentLeaderboardBinding
import com.nikola.jakshic.dagger.search.SearchFragmentDirections

class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard) {
    enum class Key { EUROPE, AMERICA, CHINA, SEA }

    companion object {
        fun setOnReselectListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            key: Key,
            listener: () -> Unit,
        ) {
            fragmentManager.setFragmentResultListener(key.name, lifecycleOwner) { _, _ ->
                listener.invoke()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLeaderboardBinding.bind(view)

        binding.toolbar.inflateMenu(R.menu.menu_home)

        binding.viewPager.adapter = RegionPagerAdapter(this)
        binding.viewPager.offscreenPageLimit = 3
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val title = when (position) {
                0 -> getString(R.string.region_europe)
                1 -> getString(R.string.region_america)
                2 -> getString(R.string.region_china)
                3 -> getString(R.string.region_sea)
                else -> throw IllegalStateException("Found more than 4 tabs.")
            }
            tab.text = title
        }.attach()

        HomeFragment.setOnReselectListener(
            parentFragmentManager,
            viewLifecycleOwner,
            HomeFragment.Key.LEADERBOARD,
        ) {
            when (binding.viewPager.currentItem) {
                0 -> childFragmentManager.setFragmentResult(Key.EUROPE.name, bundleOf())
                1 -> childFragmentManager.setFragmentResult(Key.AMERICA.name, bundleOf())
                2 -> childFragmentManager.setFragmentResult(Key.CHINA.name, bundleOf())
                3 -> childFragmentManager.setFragmentResult(Key.SEA.name, bundleOf())
                else -> throw IllegalStateException("Found more than 4 items.")
            }
        }
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
}
