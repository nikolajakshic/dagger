package com.nikola.jakshic.dagger.bookmark

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nikola.jakshic.dagger.HomeFragment
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.search.SearchFragmentDirections
import kotlinx.android.synthetic.main.fragment_bookmark.*

class BookmarkFragment : Fragment(R.layout.fragment_bookmark), HomeFragment.OnNavigationItemReselectedListener {
    private var adapter: BookmarkPagerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.inflateMenu(R.menu.menu_home)

        adapter = BookmarkPagerAdapter(requireContext(), childFragmentManager)

        viewPager.adapter = adapter
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
        val currentFragment = (adapter.instantiateItem(viewPager, position))

        (currentFragment as HomeFragment.OnNavigationItemReselectedListener).onItemReselected()
    }
}