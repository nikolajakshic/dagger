package com.nikola.jakshic.dagger.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.hide
import com.nikola.jakshic.dagger.show
import com.nikola.jakshic.dagger.ui.bookmark.BookmarkFragment
import com.nikola.jakshic.dagger.ui.competitive.CompetitiveFragment
import com.nikola.jakshic.dagger.ui.leaderboard.LeaderboardFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val competitive: Fragment
        val leaderboard: Fragment
        val bookmark: Fragment

        if (savedInstanceState == null) {
            competitive = CompetitiveFragment()
            leaderboard = LeaderboardFragment()
            bookmark = BookmarkFragment()

            supportFragmentManager.beginTransaction()
                    .add(R.id.home_fragment_container, competitive, "competitive-tag")
                    .add(R.id.home_fragment_container, leaderboard, "leaderboard-tag")
                    .add(R.id.home_fragment_container, bookmark, "bookmark-tag")
                    .hide(leaderboard)
                    .hide(bookmark)
                    .commit()
        } else {
            competitive = supportFragmentManager.findFragmentByTag("competitive-tag")!!
            leaderboard = supportFragmentManager.findFragmentByTag("leaderboard-tag")!!
            bookmark = supportFragmentManager.findFragmentByTag("bookmark-tag")!!
        }

        btmNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_competitive -> {
                    competitive.show()
                    leaderboard.hide()
                    bookmark.hide()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_leaderboard -> {
                    competitive.hide()
                    leaderboard.show()
                    bookmark.hide()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_bookmark -> {
                    competitive.hide()
                    leaderboard.hide()
                    bookmark.show()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        btmNavigation.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.action_competitive -> (competitive as CompetitiveFragment).onItemReselected()
                R.id.action_leaderboard -> (leaderboard as LeaderboardFragment).onItemReselected()
                R.id.action_bookmark -> (bookmark as BookmarkFragment).onItemReselected()
            }
        }
    }

    override fun onBackPressed() {
        if (btmNavigation.selectedItemId != R.id.action_competitive)
            btmNavigation.selectedItemId = R.id.action_competitive
        else
            super.onBackPressed()
    }

    interface OnNavigationItemReselectedListener {

        fun onItemReselected()
    }
}