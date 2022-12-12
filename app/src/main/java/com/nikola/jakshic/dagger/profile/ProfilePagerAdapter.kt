package com.nikola.jakshic.dagger.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nikola.jakshic.dagger.profile.heroes.HeroFragment
import com.nikola.jakshic.dagger.profile.matches.MatchFragment
import com.nikola.jakshic.dagger.profile.peers.PeerFragment

class ProfilePagerAdapter(
    private val accountId: Long,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MatchFragment()
            1 -> HeroFragment.newInstance(accountId)
            2 -> PeerFragment()
            else -> throw IllegalStateException("Found more than 3 tabs.")
        }
    }
}
