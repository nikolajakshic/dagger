package com.nikola.jakshic.truesight.di;

import com.nikola.jakshic.truesight.view.activity.MatchActivity;
import com.nikola.jakshic.truesight.view.activity.PlayerActivity;
import com.nikola.jakshic.truesight.view.activity.SearchActivity;
import com.nikola.jakshic.truesight.view.fragment.BookmarkFragment;
import com.nikola.jakshic.truesight.view.fragment.CompetitiveFragment;
import com.nikola.jakshic.truesight.view.fragment.HeroFragment;
import com.nikola.jakshic.truesight.view.fragment.MatchFragment;
import com.nikola.jakshic.truesight.view.fragment.PeerFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ViewModelModule.class})
public interface AppComponent {

    void inject(SearchActivity activity);

    void inject(MatchFragment fragment);

    void inject(HeroFragment fragment);

    void inject(PlayerActivity activity);

    void inject(MatchActivity activity);

    void inject(BookmarkFragment fragment);

    void inject(PeerFragment fragment);

    void inject(CompetitiveFragment fragment);
}
