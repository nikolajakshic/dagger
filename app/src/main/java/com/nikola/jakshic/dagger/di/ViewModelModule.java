package com.nikola.jakshic.dagger.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.nikola.jakshic.dagger.viewModel.RegionViewModel;
import com.nikola.jakshic.dagger.viewModel.PeerViewModel;
import com.nikola.jakshic.dagger.viewModel.BookmarkViewModel;
import com.nikola.jakshic.dagger.viewModel.CompetitiveViewModel;
import com.nikola.jakshic.dagger.viewModel.DetailViewModel;
import com.nikola.jakshic.dagger.viewModel.HeroViewModel;
import com.nikola.jakshic.dagger.viewModel.MatchDetailViewModel;
import com.nikola.jakshic.dagger.viewModel.MatchViewModel;
import com.nikola.jakshic.dagger.viewModel.SearchViewModel;
import com.nikola.jakshic.dagger.viewModel.DaggerViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MatchViewModel.class)
    ViewModel bindMatchFragmentViewModel(MatchViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HeroViewModel.class)
    ViewModel bindHeroFragmentViewModel(HeroViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    ViewModel bindPlayerViewModel(SearchViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel.class)
    ViewModel bindDetailViewModel(DetailViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BookmarkViewModel.class)
    ViewModel bindBookmarkViewModel(BookmarkViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MatchDetailViewModel.class)
    ViewModel bindMatchDetailViewModel(MatchDetailViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PeerViewModel.class)
    ViewModel bindPeerViewModel(PeerViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CompetitiveViewModel.class)
    ViewModel bindCompetitiveViewModel(CompetitiveViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegionViewModel.class)
    ViewModel bindRegionViewModel(RegionViewModel viewModel);

    @Binds
    ViewModelProvider.Factory bindViewModelFactory(DaggerViewModelFactory factory);
}