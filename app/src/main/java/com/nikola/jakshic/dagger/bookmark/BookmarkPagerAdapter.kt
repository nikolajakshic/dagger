package com.nikola.jakshic.dagger.bookmark

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.bookmark.match.MatchBookmarkFragment
import com.nikola.jakshic.dagger.bookmark.player.PlayerBookmarkFragment

class BookmarkPagerAdapter(
    private val context: Context,
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> PlayerBookmarkFragment()
            else -> MatchBookmarkFragment()
        }

    override fun getPageTitle(position: Int): CharSequence? =
        when (position) {
            0 -> context.getString(R.string.players)
            else -> context.getString(R.string.matches)
        }

    override fun getCount() = 2
}