package com.nikola.jakshic.dagger

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.nikola.jakshic.dagger.bookmark.BookmarkFragment
import com.nikola.jakshic.dagger.common.hide
import com.nikola.jakshic.dagger.common.show
import com.nikola.jakshic.dagger.competitive.CompetitiveFragment
import com.nikola.jakshic.dagger.leaderboard.LeaderboardFragment
import com.nikola.jakshic.dagger.stream.StreamFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            btmNavigation.selectedItemId = R.id.action_competitive
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val competitive = childFragmentManager.findFragmentByTag("competitive") ?: CompetitiveFragment()
        val leaderboard = childFragmentManager.findFragmentByTag("leaderboard") ?: LeaderboardFragment()
        val bookmark = childFragmentManager.findFragmentByTag("bookmark") ?: BookmarkFragment()
        val stream = childFragmentManager.findFragmentByTag("stream") ?: StreamFragment()

        if (childFragmentManager.findFragmentByTag("competitive") == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, competitive, "competitive")
                .add(R.id.fragment_container_view, leaderboard, "leaderboard")
                .add(R.id.fragment_container_view, bookmark, "bookmark")
                .add(R.id.fragment_container_view, stream, "stream")
                .hide(leaderboard)
                .hide(bookmark)
                .hide(stream)
                .commit()
        }

        btmNavigation.setOnNavigationItemSelectedListener {
            onBackPressedCallback.isEnabled = it.itemId != R.id.action_competitive
            if (it.itemId == R.id.action_competitive) competitive.show() else competitive.hide()
            if (it.itemId == R.id.action_leaderboard) leaderboard.show() else leaderboard.hide()
            if (it.itemId == R.id.action_bookmark) bookmark.show() else bookmark.hide()
            if (it.itemId == R.id.action_stream) stream.show() else stream.hide()
            return@setOnNavigationItemSelectedListener true
        }

        btmNavigation.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.action_competitive -> (competitive as CompetitiveFragment).onItemReselected()
                R.id.action_leaderboard -> (leaderboard as LeaderboardFragment).onItemReselected()
                R.id.action_bookmark -> (bookmark as BookmarkFragment).onItemReselected()
                R.id.action_stream -> (stream as StreamFragment).onItemReselected()
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Get BottomNavigationView's selectedItemId up-to-date value
        onBackPressedCallback.isEnabled = btmNavigation.selectedItemId != R.id.action_competitive
    }

    interface OnNavigationItemReselectedListener {
        fun onItemReselected()
    }
}