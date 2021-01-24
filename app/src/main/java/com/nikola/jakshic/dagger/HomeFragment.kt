package com.nikola.jakshic.dagger

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.nikola.jakshic.dagger.bookmark.BookmarkFragment
import com.nikola.jakshic.dagger.competitive.CompetitiveFragment
import com.nikola.jakshic.dagger.leaderboard.LeaderboardFragment
import com.nikola.jakshic.dagger.stream.StreamFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    private lateinit var competitive: CompetitiveFragment
    private lateinit var leaderboard: LeaderboardFragment
    private lateinit var bookmark: BookmarkFragment
    private lateinit var stream: StreamFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            competitive = CompetitiveFragment()
            leaderboard = LeaderboardFragment()
            bookmark = BookmarkFragment()
            stream = StreamFragment()

            childFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, competitive, "competitive")
                .add(R.id.fragment_container_view, leaderboard, "leaderboard")
                .add(R.id.fragment_container_view, bookmark, "bookmark")
                .add(R.id.fragment_container_view, stream, "stream")
                .detach(leaderboard)
                .detach(bookmark)
                .detach(stream)
                .commit()
        } else {
            competitive = childFragmentManager.findFragmentByTag("competitive") as CompetitiveFragment
            leaderboard = childFragmentManager.findFragmentByTag("leaderboard") as LeaderboardFragment
            bookmark = childFragmentManager.findFragmentByTag("bookmark") as BookmarkFragment
            stream = childFragmentManager.findFragmentByTag("stream") as StreamFragment
        }

        onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            btmNavigation.selectedItemId = R.id.action_competitive
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btmNavigation.setOnNavigationItemSelectedListener {
            onBackPressedCallback.isEnabled = it.itemId != R.id.action_competitive
            if (it.itemId == R.id.action_competitive) selectFragment(competitive)
            if (it.itemId == R.id.action_leaderboard) selectFragment(leaderboard)
            if (it.itemId == R.id.action_bookmark) selectFragment(bookmark)
            if (it.itemId == R.id.action_stream) selectFragment(stream)
            return@setOnNavigationItemSelectedListener true
        }

        btmNavigation.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.action_competitive -> competitive.onItemReselected()
                R.id.action_leaderboard -> leaderboard.onItemReselected()
                R.id.action_bookmark -> bookmark.onItemReselected()
                R.id.action_stream -> stream.onItemReselected()
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Get BottomNavigationView's selectedItemId up-to-date value
        onBackPressedCallback.isEnabled = btmNavigation.selectedItemId != R.id.action_competitive
    }

    private fun selectFragment(selectedFragment: Fragment) {
        val fragments = arrayOf<Fragment>(competitive, leaderboard, bookmark, stream)
        val transaction = childFragmentManager.beginTransaction()
        fragments.forEach {
            if (selectedFragment == it) {
                transaction.attach(selectedFragment)
            } else {
                transaction.detach(it)
            }
        }
        transaction.commit()
    }

    interface OnNavigationItemReselectedListener {
        fun onItemReselected()
    }
}