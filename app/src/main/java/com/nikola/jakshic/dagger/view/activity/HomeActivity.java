package com.nikola.jakshic.dagger.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nikola.jakshic.dagger.R;
import com.nikola.jakshic.dagger.view.fragment.BookmarkFragment;
import com.nikola.jakshic.dagger.view.fragment.CompetitiveFragment;
import com.nikola.jakshic.dagger.view.fragment.LeaderboardFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Title");
        BottomNavigationView btmNavView = findViewById(R.id.btm_navigation_home);

        Fragment competitive;
        Fragment bookmark;
        Fragment leaderboard;

        if (savedInstanceState == null) {
            competitive = new CompetitiveFragment();
            bookmark = new BookmarkFragment();
            leaderboard = new LeaderboardFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.home_fragment_container, bookmark, "bookmark-tag")
                    .hide(bookmark)
                    .commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.home_fragment_container, leaderboard, "leaderboard-tag")
                    .hide(leaderboard)
                    .commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.home_fragment_container, competitive, "competitive-tag")
                    .commit();
        } else {
            competitive = getSupportFragmentManager().findFragmentByTag("competitive-tag");
            bookmark = getSupportFragmentManager().findFragmentByTag("bookmark-tag");
            leaderboard = getSupportFragmentManager().findFragmentByTag("leaderboard-tag");
        }

        btmNavView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_competitive:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .show(competitive)
                            .commit();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .hide(leaderboard)
                            .commit();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .hide(bookmark)
                            .commit();
                    setTitle("Competitive");
                    return true;
                case R.id.action_leaderboard:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .hide(competitive)
                            .commit();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .show(leaderboard)
                            .commit();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .hide(bookmark)
                            .commit();
                    setTitle("Leaderboard");
                    return true;
                case R.id.action_bookmark:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .hide(competitive)
                            .commit();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .hide(leaderboard)
                            .commit();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .show(bookmark)
                            .commit();
                    setTitle("Bookmark");
                    return true;
            }
            return false;
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_home_search:
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.menu_home_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}