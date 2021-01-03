package com.nikola.jakshic.dagger

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        btmNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.competitiveFragment ||
                destination.id == R.id.leaderboardFragment ||
                destination.id == R.id.bookmarkFragment ||
                destination.id == R.id.streamFragment
            ) {
                btmNavigation.visibility = View.VISIBLE
            } else {
                btmNavigation.visibility = View.GONE
            }
        }
    }

    interface OnNavigationItemReselectedListener {
        fun onItemReselected()
    }
}