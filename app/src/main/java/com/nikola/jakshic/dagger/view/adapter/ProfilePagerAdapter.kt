package com.nikola.jakshic.dagger.view.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.nikola.jakshic.dagger.view.fragment.HeroFragment
import com.nikola.jakshic.dagger.view.fragment.MatchFragment
import com.nikola.jakshic.dagger.view.fragment.PeerFragment

class ProfilePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) =
            when (position) {
                0 -> MatchFragment()
                1 -> HeroFragment()
                else -> PeerFragment()
            }

    override fun getPageTitle(position: Int) =
            when (position) {
                0 -> "MATCHES"
                1 -> "HEROES"
                else -> "PEERS"
            }

    override fun getCount() = 3
}