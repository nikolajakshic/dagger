package com.nikola.jakshic.truesight.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.nikola.jakshic.truesight.DetailViewModel;
import com.nikola.jakshic.truesight.HeroFragmentViewModel;
import com.nikola.jakshic.truesight.HomeViewModel;
import com.nikola.jakshic.truesight.MatchFragmentViewModel;
import com.nikola.jakshic.truesight.PlayerViewModel;
import com.nikola.jakshic.truesight.TrueSightViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MatchFragmentViewModel.class)
    ViewModel bindMatchFragmentViewModel(MatchFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HeroFragmentViewModel.class)
    ViewModel bindHeroFragmentViewModel(HeroFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel.class)
    ViewModel bindPlayerViewModel(PlayerViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel.class)
    ViewModel bindDetailViewModel(DetailViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    ViewModel bindHomeViewModel(HomeViewModel viewModel);

    @Binds
    ViewModelProvider.Factory bindViewModelFactory(TrueSightViewModelFactory factory);
}