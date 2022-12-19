package com.nikola.jakshic.dagger.bookmark

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nikola.jakshic.dagger.bookmark.match.MatchBookmarkFragment
import com.nikola.jakshic.dagger.bookmark.player.PlayerBookmarkFragment

class BookmarkPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PlayerBookmarkFragment()
            1 -> MatchBookmarkFragment()
            else -> throw IllegalStateException("Found more than 2 items.")
        }
    }
}
