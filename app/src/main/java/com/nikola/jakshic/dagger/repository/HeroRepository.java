package com.nikola.jakshic.dagger.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.nikola.jakshic.dagger.AppExecutors;
import com.nikola.jakshic.dagger.Status;
import com.nikola.jakshic.dagger.data.local.HeroDao;
import com.nikola.jakshic.dagger.data.remote.OpenDotaService;
import com.nikola.jakshic.dagger.model.Hero;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class HeroRepository {

    private OpenDotaService service;
    private HeroDao heroDao;
    private AppExecutors executor;
    private PagedList.Config config;

    @Inject
    public HeroRepository(AppExecutors executor, OpenDotaService service, HeroDao heroDao) {
        this.service = service;
        this.executor = executor;
        this.heroDao = heroDao;
        // TODO this should be provided by dagger
        config = new PagedList.Config.Builder()
                .setPrefetchDistance(15)
                .setInitialLoadSizeHint(80)
                .setPageSize(40)
                .setEnablePlaceholders(false).build();
    }

    // PagedList is not mutable, so we can not sort the items, but
    // instead we need to request new sorted data from the database
    public LiveData<PagedList<Hero>> fetchByGames(long id) {
        DataSource.Factory<Integer, Hero> factory = heroDao.getHeroesByGames(id);
        return new LivePagedListBuilder<>(factory, config).build();
    }

    // PagedList is not mutable, so we can not sort the items, but
    // instead we need to request new sorted data from the database
    public LiveData<PagedList<Hero>> fetchByWinrate(long id) {
        DataSource.Factory<Integer, Hero> factory = heroDao.getHeroesByWinrate(id);
        return new LivePagedListBuilder<>(factory, config).build();
    }

    // PagedList is not mutable, so we can not sort the items, but
    // instead we need to request new sorted data from the database
    public LiveData<PagedList<Hero>> fetchByWins(long id) {
        DataSource.Factory<Integer, Hero> factory = heroDao.getHeroesByWins(id);
        return new LivePagedListBuilder<>(factory, config).build();
    }

    // PagedList is not mutable, so we can not sort the items, but
    // instead we need to request new sorted data from the database
    public LiveData<PagedList<Hero>> fetchByLosses(long id) {
        DataSource.Factory<Integer, Hero> factory = heroDao.getHeroesByLosses(id);
        return new LivePagedListBuilder<>(factory, config).build();
    }

    /**
     * Fetches data from the network and inserts it into database
     *
     * @param status observed by the UI, so it can show proper progress bar
     * @param id player id
     */
    public void fetchHeroes(MutableLiveData<Status> status, long id) {
        status.setValue(Status.LOADING);
        service.getHeroes(id).enqueue(new Callback<List<Hero>>() {
            @Override
            public void onResponse(Call<List<Hero>> call, Response<List<Hero>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    executor.diskIO().execute(() -> {
                        for (Hero item : response.body())

                            // Json response doesn't contain player id
                            // so we need to set it manually
                            item.setAccountId(id);

                        heroDao.insertHeroes(response.body());
                        status.postValue(Status.SUCCESS);
                    });
                } else
                    status.setValue(Status.ERROR);
            }

            @Override
            public void onFailure(Call<List<Hero>> call, Throwable t) {
                status.setValue(Status.ERROR);
            }
        });
    }
}