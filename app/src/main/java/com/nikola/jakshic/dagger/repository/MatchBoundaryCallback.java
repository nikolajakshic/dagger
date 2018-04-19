package com.nikola.jakshic.dagger.repository;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.nikola.jakshic.dagger.AppExecutors;
import com.nikola.jakshic.dagger.Status;
import com.nikola.jakshic.dagger.data.local.MatchDao;
import com.nikola.jakshic.dagger.data.remote.OpenDotaService;
import com.nikola.jakshic.dagger.model.match.Match;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchBoundaryCallback extends PagedList.BoundaryCallback<Match> {

    private OpenDotaService service;
    private MatchDao matchDao;
    private AppExecutors executor;
    private MutableLiveData<Status> status;
    private long accountId;

    public MatchBoundaryCallback(OpenDotaService service,
                                 MatchDao matchDao,
                                 AppExecutors executor,
                                 MutableLiveData<Status> status,
                                 long accountId) {
        this.service = service;
        this.matchDao = matchDao;
        this.executor = executor;
        this.status = status;
        this.accountId = accountId;
    }

    @Override
    public void onZeroItemsLoaded() {

        status.setValue(Status.LOADING);
        service.getMatches(accountId, 80, 0).enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                if (response.body() != null && response.isSuccessful())
                    executor.diskIO().execute(() -> {
                        for (Match match : response.body())
                            match.setAccountId(accountId);

                        matchDao.insertMatches(response.body());
                        status.postValue(Status.SUCCESS);
                    });
                else
                    status.setValue(Status.ERROR);
            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {
                status.setValue(Status.ERROR);
            }
        });
    }

    /*
     * When prefetch distance has been reached, calculate the offset, and
     * fetch more data from the network
     */
    @Override
    public void onItemAtEndLoaded(@NonNull Match itemAtEnd) {

        executor.diskIO().execute(() -> {
            long offset = matchDao.getMatchCount(accountId);

            status.postValue(Status.LOADING);
            service.getMatches(accountId, 40, offset).enqueue(new Callback<List<Match>>() {
                @Override
                public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {
                    if (response.body() != null && response.isSuccessful())
                        executor.diskIO().execute(() -> {
                            for (Match match : response.body())
                                match.setAccountId(accountId);

                            matchDao.insertMatches(response.body());
                            status.postValue(Status.SUCCESS);
                        });
                    else
                        status.setValue(Status.ERROR);
                }

                @Override
                public void onFailure(Call<List<Match>> call, Throwable t) {
                    status.setValue(Status.ERROR);
                }
            });
        });
    }
}