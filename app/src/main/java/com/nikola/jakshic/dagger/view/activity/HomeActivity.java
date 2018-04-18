package com.nikola.jakshic.dagger.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.home_fragment_container, competitive, "competitive-tag")
                    .add(R.id.home_fragment_container, leaderboard, "leaderboard-tag")
                    .add(R.id.home_fragment_container, bookmark, "bookmark-tag")
                    .hide(bookmark)
                    .hide(leaderboard)
                    .commit();
        } else {
            competitive = getSupportFragmentManager().findFragmentByTag("competitive-tag");
            bookmark = getSupportFragmentManager().findFragmentByTag("bookmark-tag");
            leaderboard = getSupportFragmentManager().findFragmentByTag("leaderboard-tag");
        }

        btmNavView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_competitive:
                    showFragment(competitive, true);
                    showFragment(leaderboard, false);
                    showFragment(bookmark, false);
                    setTitle("Competitive");
                    return true;
                case R.id.action_leaderboard:
                    showFragment(competitive, false);
                    showFragment(leaderboard, true);
                    showFragment(bookmark, false);
                    setTitle("Leaderboard");
                    return true;
                case R.id.action_bookmark:
                    showFragment(competitive, false);
                    showFragment(leaderboard, false);
                    showFragment(bookmark, true);
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

    private void showFragment(Fragment fragment, boolean show) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (show)
            transaction.show(fragment);
        else
            transaction.hide(fragment);
        transaction.commit();
    }
}