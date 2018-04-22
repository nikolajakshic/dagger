package com.nikola.jakshic.dagger.di;

import com.nikola.jakshic.dagger.view.activity.MatchActivity;
import com.nikola.jakshic.dagger.view.activity.PlayerActivity;
import com.nikola.jakshic.dagger.view.activity.SearchActivity;
import com.nikola.jakshic.dagger.view.activity.SettingsActivity;
import com.nikola.jakshic.dagger.view.fragment.BookmarkFragment;
import com.nikola.jakshic.dagger.ui.competitive.CompetitiveFragment;
import com.nikola.jakshic.dagger.view.fragment.HeroFragment;
import com.nikola.jakshic.dagger.view.fragment.MatchFragment;
import com.nikola.jakshic.dagger.view.fragment.PeerFragment;
import com.nikola.jakshic.dagger.view.fragment.RegionFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DbModule.class, ViewModelModule.class})
public interface AppComponent {

    void inject(SearchActivity activity);

    void inject(MatchFragment fragment);

    void inject(HeroFragment fragment);

    void inject(PlayerActivity activity);

    void inject(MatchActivity activity);

    void inject(BookmarkFragment fragment);

    void inject(PeerFragment fragment);

    void inject(CompetitiveFragment fragment);

    void inject(RegionFragment fragment);

    void inject(SettingsActivity activity);
}