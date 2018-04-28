package com.nikola.jakshic.dagger.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.nikola.jakshic.dagger.DaggerViewModelFactory
import com.nikola.jakshic.dagger.ui.bookmark.BookmarkViewModel
import com.nikola.jakshic.dagger.ui.competitive.CompetitiveViewModel
import com.nikola.jakshic.dagger.ui.leaderboard.RegionViewModel
import com.nikola.jakshic.dagger.ui.match.MatchDetailViewModel
import com.nikola.jakshic.dagger.ui.profile.ProfileViewModel
import com.nikola.jakshic.dagger.ui.profile.heroes.HeroViewModel
import com.nikola.jakshic.dagger.ui.profile.matches.MatchViewModel
import com.nikola.jakshic.dagger.ui.profile.peers.PeerViewModel
import com.nikola.jakshic.dagger.ui.search.SearchViewModel
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
    @ViewModelKey(MatchDetailViewModel::class)
    fun bindMatchDetailViewModel(viewModel: MatchDetailViewModel): ViewModel

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
    fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}