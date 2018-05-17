package com.nikola.jakshic.dagger.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.R.id.btmNavigation
import com.nikola.jakshic.dagger.hide
import com.nikola.jakshic.dagger.show
import com.nikola.jakshic.dagger.ui.search.SearchActivity
import com.nikola.jakshic.dagger.ui.bookmark.BookmarkFragment
import com.nikola.jakshic.dagger.ui.competitive.CompetitiveFragment
import com.nikola.jakshic.dagger.ui.leaderboard.LeaderboardFragment
import com.nikola.jakshic.dagger.ui.settings.SettingsActivity
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
            competitive = supportFragmentManager.findFragmentByTag("competitive-tag")
            leaderboard = supportFragmentManager.findFragmentByTag("leaderboard-tag")
            bookmark = supportFragmentManager.findFragmentByTag("bookmark-tag")
        }

        btmNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_competitive -> {
                    competitive.show()
                    leaderboard.hide()
                    bookmark.hide()
                    title = "Competitive"
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_leaderboard -> {
                    competitive.hide()
                    leaderboard.show()
                    bookmark.hide()
                    title = "Leaderboard"
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_bookmark -> {
                    competitive.hide()
                    leaderboard.hide()
                    bookmark.show()
                    title = "Bookmark"
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_home_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            R.id.menu_home_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
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