package com.nikola.jakshic.truesight.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nikola.jakshic.truesight.view.fragment.HeroFragment;
import com.nikola.jakshic.truesight.view.fragment.MatchFragment;
import com.nikola.jakshic.truesight.view.fragment.PeerFragment;

public class PlayerPagerAdapter extends FragmentPagerAdapter {

    public PlayerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MatchFragment();
            case 1:
                return new HeroFragment();
            case 2:
                return new PeerFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0)
            return "MATCHES";
        else if (position == 1)
            return "HEROES";
        else
            return "PEERS";
    }
}