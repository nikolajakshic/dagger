package com.nikola.jakshic.dagger.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.nikola.jakshic.dagger.Status;
import com.nikola.jakshic.dagger.data.local.CompetitiveDao;
import com.nikola.jakshic.dagger.data.remote.OpenDotaService;
import com.nikola.jakshic.dagger.model.Competitive;
import com.nikola.jakshic.dagger.model.match.Match;

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

    @Inject
    public MatchRepository(OpenDotaService service, CompetitiveDao competitiveDao) {
        this.service = service;
        this.competitiveDao = competitiveDao;
    }

    public void fetchMatches(MutableLiveData<List<Match>> list, MutableLiveData<Boolean> loading, long id) {
        loading.setValue(true);
        service.getMatches(id).enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                list.setValue(response.body());
                loading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }

    public void fetchMatchData(MutableLiveData<Match> match, MutableLiveData<Boolean> loading, long matchId) {
        loading.setValue(true);
        service.getMatch(matchId).enqueue(new Callback<Match>() {
            @Override
            public void onResponse(Call<Match> call, Response<Match> response) {
                match.setValue(response.body());
                loading.setValue(false);
            }

            @Override
            public void onFailure(Call<Match> call, Throwable t) {
                loading.setValue(false);
            }
        });
    }

    public LiveData<PagedList<Competitive>> getCompetitiveMatches() {
        DataSource.Factory<Integer, Competitive> factory = competitiveDao.getMatches();
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(40)
                .setPrefetchDistance(15)
                .setInitialLoadSizeHint(80)
                .build();

        return new LivePagedListBuilder<>(factory, config).build();
    }

    public void fetchCompetitiveMatches(MutableLiveData<Status> status) {
        service.getCompetitiveMatches().enqueue(new Callback<List<Competitive>>() {
            @Override
            public void onResponse(Call<List<Competitive>> call, Response<List<Competitive>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    competitiveDao.insertMatches(response.body());
                    status.setValue(Status.SUCCESS);
                } else
                    status.setValue(Status.ERROR);
            }

            @Override
            public void onFailure(Call<List<Competitive>> call, Throwable t) {
                status.setValue(Status.ERROR);
            }
        });
    }
}