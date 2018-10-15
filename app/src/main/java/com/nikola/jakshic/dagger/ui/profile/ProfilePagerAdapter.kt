package com.nikola.jakshic.dagger.ui.profile

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nikola.jakshic.dagger.R
import com.nikola.jakshic.dagger.ui.profile.heroes.HeroFragment
import com.nikola.jakshic.dagger.ui.profile.matches.MatchFragment
import com.nikola.jakshic.dagger.ui.profile.peers.PeerFragment

class ProfilePagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int) =
            when (position) {
                0 -> MatchFragment()
                1 -> HeroFragment()
                else -> PeerFragment()
            }

    override fun getPageTitle(position: Int) =
            when (position) {
                0 -> context.getString(R.string.matches)
                1 -> context.getString(R.string.heroes)
                else -> context.getString(R.string.peers)
            }

    override fun getCount() = 3
}