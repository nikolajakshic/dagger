package com.nikola.jakshic.dagger.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.nikola.jakshic.dagger.AppExecutors;
import com.nikola.jakshic.dagger.Status;
import com.nikola.jakshic.dagger.data.local.CompetitiveDao;
import com.nikola.jakshic.dagger.data.local.DotaDatabase;
import com.nikola.jakshic.dagger.data.local.MatchDao;
import com.nikola.jakshic.dagger.data.remote.OpenDotaService;
import com.nikola.jakshic.dagger.model.match.Match;
import com.nikola.jakshic.dagger.model.match.MatchStats;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class MatchRepository {

    private OpenDotaService service;
    private CompetitiveDao competitiveDao;
    private DotaDatabase db;
    private MatchDao matchDao;
    private AppExecutors executor;
    private PagedList.Config config;

    @Inject
    public MatchRepository(OpenDotaService service,
                           AppExecutors executor,
                           DotaDatabase db,
                           CompetitiveDao competitiveDao,
                           MatchDao matchDao) {
        this.service = service;
        this.db = db;
        this.executor = executor;
        this.competitiveDao = competitiveDao;
        this.matchDao = matchDao;
        // TODO should be provided by dagger
        config = new PagedList.Config.Builder()
                .setPrefetchDistance(15)
                .setInitialLoadSizeHint(80)
                .setPageSize(40)
                .setEnablePlaceholders(false).build();
    }

    public LiveData<PagedList<Match>> getMatches(MutableLiveData<Status> status,long accountId) {
        DataSource.Factory<Integer, Match> factory = matchDao.getMatches(accountId);
        return new LivePagedListBuilder<>(factory, config)
                .setBoundaryCallback(new MatchBoundaryCallback(
                        service,
                        matchDao,
                        executor,
                        status,
                        accountId)).build();
    }

    /*
     * Refreshing data by deleting matches from the database
     * and fetching the same amount from the network
     */
    public void refreshMatches(MutableLiveData<Status> status, long id) {

        executor.diskIO().execute(() -> {
            long matches = matchDao.getMatchCount(id);

            // If there are no matches to refresh, MatchBoundaryCallback.OnZeroItemsLoaded is called to
            // fetch data from the network, so we can exit from this method
            if (matches == 0) return;

            status.postValue(Status.LOADING);
            service.getMatches(id, matches, 0).enqueue(new Callback<List<Match>>() {
                @Override
                public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {

                    if (response.body() != null && response.isSuccessful()) {

                        executor.diskIO().execute(() -> {
                            for (Match match : response.body())
                                match.setAccountId(id);

                            db.runInTransaction(() -> {
                                matchDao.deleteMatches(id);
                                matchDao.insertMatches(response.body());
                            });

                            status.postValue(Status.SUCCESS);
                        });
                    }
                    status.setValue(Status.ERROR);
                }

                @Override
                public void onFailure(Call<List<Match>> call, Throwable t) {
                    status.setValue(Status.ERROR);
                }
            });
        });
    }

    public void fetchMatchData(MutableLiveData<MatchStats> match, MutableLiveData<Boolean> loading, long matchId) {
        loading.setValue(true);
        service.getMatch(matchId).enqueue(new Callback<MatchStats>() {
            @Override
            public void onResponse(Call<MatchStats> call, Response<MatchStats> response) {
                match.setValue(response.body());
                loading.setValue(false);
            }

            @Override
            public void onFailure(Call<MatchStats> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }
}