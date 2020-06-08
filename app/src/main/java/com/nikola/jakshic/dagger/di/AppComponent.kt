package com.nikola.jakshic.dagger.di

import com.nikola.jakshic.dagger.DaggerApp
import com.nikola.jakshic.dagger.bookmark.player.PlayerBookmarkFragment
import com.nikola.jakshic.dagger.competitive.CompetitiveFragment
import com.nikola.jakshic.dagger.leaderboard.RegionFragment
import com.nikola.jakshic.dagger.matchstats.MatchStatsActivity
import com.nikola.jakshic.dagger.profile.ProfileActivity
import com.nikola.jakshic.dagger.profile.heroes.HeroFragment
import com.nikola.jakshic.dagger.profile.matches.MatchFragment
import com.nikola.jakshic.dagger.profile.matches.byhero.MatchesByHeroActivity
import com.nikola.jakshic.dagger.profile.peers.PeerFragment
import com.nikola.jakshic.dagger.search.SearchActivity
import com.nikola.jakshic.dagger.settings.SettingsActivity
import com.nikola.jakshic.dagger.stream.StreamFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(DbModule::class), (ViewModelModule::class), (CoilModule::class)])
interface AppComponent {
    fun inject(application: DaggerApp)

    fun inject(activity: SearchActivity)

    fun inject(fragment: MatchFragment)

    fun inject(fragment: HeroFragment)

    fun inject(activity: MatchStatsActivity)

    fun inject(fragment: PlayerBookmarkFragment)

    fun inject(fragment: PeerFragment)

    fun inject(fragment: CompetitiveFragment)

    fun inject(fragment: RegionFragment)

    fun inject(activity: SettingsActivity)

    fun inject(activity: ProfileActivity)

    fun inject(activity: MatchesByHeroActivity)

    fun inject(fragment: StreamFragment)
}