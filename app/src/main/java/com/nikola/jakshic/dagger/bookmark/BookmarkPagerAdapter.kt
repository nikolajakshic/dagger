package com.nikola.jakshic.dagger.bookmark

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nikola.jakshic.dagger.bookmark.player.PlayerBookmarkFragment

class BookmarkPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = PlayerBookmarkFragment()

    override fun getPageTitle(position: Int): CharSequence? = "Players"

    override fun getCount() = 1
}