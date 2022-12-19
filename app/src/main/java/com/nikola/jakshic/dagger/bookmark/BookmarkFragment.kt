package com.nikola.jakshic.dagger.bookmark

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
import com.nikola.jakshic.dagger.databinding.FragmentBookmarkBinding
import com.nikola.jakshic.dagger.search.SearchFragmentDirections

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    enum class Key { PLAYERS, MATCHES }

    companion object {
        fun setOnReselectListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            key: Key,
            listener: () -> Unit
        ) {
            fragmentManager.setFragmentResultListener(key.name, lifecycleOwner) { _, _ ->
                listener.invoke()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBookmarkBinding.bind(view)

        binding.toolbar.inflateMenu(R.menu.menu_home)

        binding.viewPager.adapter = BookmarkPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val title = when (position) {
                0 -> getString(R.string.players)
                1 -> getString(R.string.matches)
                else -> throw IllegalStateException("Found more than 2 tabs.")
            }
            tab.text = title
        }.attach()

        HomeFragment.setOnReselectListener(
            parentFragmentManager,
            viewLifecycleOwner,
            HomeFragment.Key.BOOKMARK
        ) {
            when (binding.viewPager.currentItem) {
                0 -> childFragmentManager.setFragmentResult(Key.PLAYERS.name, bundleOf())
                1 -> childFragmentManager.setFragmentResult(Key.MATCHES.name, bundleOf())
                else -> throw IllegalArgumentException("Found more than 2 items.")
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
