package com.nikola.jakshic.dagger.di

import com.nikola.jakshic.dagger.ui.bookmark.BookmarkFragment
import com.nikola.jakshic.dagger.ui.competitive.CompetitiveFragment
import com.nikola.jakshic.dagger.ui.leaderboard.RegionFragment
import com.nikola.jakshic.dagger.ui.matchstats.MatchStatsActivity
import com.nikola.jakshic.dagger.ui.profile.ProfileActivity
import com.nikola.jakshic.dagger.ui.profile.heroes.HeroFragment
import com.nikola.jakshic.dagger.ui.profile.matches.MatchFragment
import com.nikola.jakshic.dagger.ui.profile.matches.byhero.MatchesByHeroActivity
import com.nikola.jakshic.dagger.ui.profile.peers.PeerFragment
import com.nikola.jakshic.dagger.ui.search.SearchActivity
import com.nikola.jakshic.dagger.ui.settings.SettingsActivity
import com.nikola.jakshic.dagger.ui.stream.StreamFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(DbModule::class), (ViewModelModule::class)])
interface AppComponent {

    fun inject(activity: SearchActivity)

    fun inject(fragment: MatchFragment)

    fun inject(fragment: HeroFragment)

    fun inject(activity: MatchStatsActivity)

    fun inject(fragment: BookmarkFragment)

    fun inject(fragment: PeerFragment)

    fun inject(fragment: CompetitiveFragment)

    fun inject(fragment: RegionFragment)

    fun inject(activity: SettingsActivity)

    fun inject(activity: ProfileActivity)

    fun inject(activity: MatchesByHeroActivity)

    fun inject(fragment: StreamFragment)
}