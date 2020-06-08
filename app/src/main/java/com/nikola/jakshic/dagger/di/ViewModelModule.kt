package com.nikola.jakshic.dagger.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikola.jakshic.dagger.bookmark.player.BookmarkViewModel
import com.nikola.jakshic.dagger.common.DaggerViewModelFactory
import com.nikola.jakshic.dagger.competitive.CompetitiveViewModel
import com.nikola.jakshic.dagger.leaderboard.RegionViewModel
import com.nikola.jakshic.dagger.matchstats.MatchStatsViewModel
import com.nikola.jakshic.dagger.profile.ProfileViewModel
import com.nikola.jakshic.dagger.profile.heroes.HeroViewModel
import com.nikola.jakshic.dagger.profile.matches.MatchViewModel
import com.nikola.jakshic.dagger.profile.matches.byhero.MatchesByHeroViewModel
import com.nikola.jakshic.dagger.profile.peers.PeerViewModel
import com.nikola.jakshic.dagger.search.SearchViewModel
import com.nikola.jakshic.dagger.stream.StreamViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MatchViewModel::class)
    fun bindMatchFragmentViewModel(viewModel: MatchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HeroViewModel::class)
    fun bindHeroFragmentViewModel(viewModel: HeroViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindPlayerViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BookmarkViewModel::class)
    fun bindBookmarkViewModel(viewModel: BookmarkViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MatchStatsViewModel::class)
    fun bindMatchDetailViewModel(viewModel: MatchStatsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PeerViewModel::class)
    fun bindPeerViewModel(viewModel: PeerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompetitiveViewModel::class)
    fun bindCompetitiveViewModel(viewModel: CompetitiveViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegionViewModel::class)
    fun bindRegionViewModel(viewModel: RegionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MatchesByHeroViewModel::class)
    fun bindMatchesByHeroViewModel(viewModel: MatchesByHeroViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StreamViewModel::class)
    fun bindStreamViewModel(viewModel: StreamViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}