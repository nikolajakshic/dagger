package com.nikola.jakshic.truesight.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.nikola.jakshic.truesight.PeerViewModel;
import com.nikola.jakshic.truesight.viewModel.MatchDetailViewModel;
import com.nikola.jakshic.truesight.viewModel.BookmarkViewModel;
import com.nikola.jakshic.truesight.viewModel.DetailViewModel;
import com.nikola.jakshic.truesight.viewModel.HeroViewModel;
import com.nikola.jakshic.truesight.viewModel.MatchViewModel;
import com.nikola.jakshic.truesight.viewModel.SearchViewModel;
import com.nikola.jakshic.truesight.viewModel.TrueSightViewModelFactory;

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
    ViewModelProvider.Factory bindViewModelFactory(TrueSightViewModelFactory factory);
}