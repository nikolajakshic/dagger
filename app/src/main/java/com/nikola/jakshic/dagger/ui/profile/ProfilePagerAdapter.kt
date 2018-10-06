package com.nikola.jakshic.dagger.ui.profile

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nikola.jakshic.dagger.ui.profile.heroes.HeroFragment
import com.nikola.jakshic.dagger.ui.profile.matches.MatchFragment
import com.nikola.jakshic.dagger.ui.profile.peers.PeerFragment

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