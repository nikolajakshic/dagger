package com.nikola.jakshic.truesight.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.nikola.jakshic.truesight.HeroFragmentViewModel;
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
    ViewModel bindMatchViewModel(MatchFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HeroFragmentViewModel.class)
    ViewModel bindHeroViewModel(HeroFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel.class)
    ViewModel bindPlayerViewModel(PlayerViewModel viewModel);

    @Binds
    ViewModelProvider.Factory bindViewModelFactory(TrueSightViewModelFactory factory);
}