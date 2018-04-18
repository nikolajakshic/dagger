package com.nikola.jakshic.dagger.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.crashlytics.android.Crashlytics;
import com.nikola.jakshic.dagger.AppExecutors;
import com.nikola.jakshic.dagger.Status;
import com.nikola.jakshic.dagger.data.local.PeerDao;
import com.nikola.jakshic.dagger.data.remote.OpenDotaService;
import com.nikola.jakshic.dagger.model.Peer;

import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class PeerRepository {

    private OpenDotaService service;
    private AppExecutors executor;
    private PeerDao peerDao;
    private PagedList.Config config;

    @Inject
    public PeerRepository(AppExecutors executor, OpenDotaService service, PeerDao peerDao) {
        this.service = service;
        this.executor = executor;
        this.peerDao = peerDao;
        // TODO THIS SHOULD BE PROVIDED BY DAGGER
        config = new PagedList.Config.Builder()
                .setPrefetchDistance(15)
                .setInitialLoadSizeHint(80)
                .setPageSize(40)
                .setEnablePlaceholders(false).build();
    }

    public LiveData<PagedList<Peer>> fetchByGames(long id) {
        DataSource.Factory<Integer, Peer> factory = peerDao.getByGames(id);
        return new LivePagedListBuilder<>(factory, config).build();
    }

    public LiveData<PagedList<Peer>> fetchByWinrate(long id) {
        DataSource.Factory<Integer, Peer> factory = peerDao.getByWinrate(id);
        return new LivePagedListBuilder<>(factory, config).build();
    }

    public void fetchPeers(MutableLiveData<Status> status, long id) {
        status.setValue(Status.LOADING);
        service.getPeers(id).enqueue(new Callback<List<Peer>>() {
            @Override
            public void onResponse(Call<List<Peer>> call, Response<List<Peer>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    executor.diskIO().execute(() -> {
                        ListIterator<Peer> iterator = response.body().listIterator();

                        // It's impossible to iterate through the list and remove elements
                        // because it will skip next element, so we need to use iterator
                        while (iterator.hasNext()) {
                            Peer peer = iterator.next();
                            // Remove enemy players from peer list
                            if (peer.getWithGames() == 0)
                                iterator.remove();

                            // Only peer_id is fetched from the network,
                            // we need to pass player id manually
                            peer.setAccountId(id);
                        }

                        peerDao.insertPeers(response.body());
                        status.postValue(Status.SUCCESS);
                    });
                } else
                    status.setValue(Status.ERROR);
            }

            @Override
            public void onFailure(Call<List<Peer>> call, Throwable t) {
                status.setValue(Status.ERROR);
            }
        });
    }
}