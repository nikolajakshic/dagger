package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.nikola.jakshic.dagger.data.local.MatchDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Match
import com.nikola.jakshic.dagger.vo.MatchStats
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchRepository @Inject constructor(
        private val service: OpenDotaService,
        private val dao: MatchDao) {

    fun getMatchesFromDb(
            id: Long,
            status: MutableLiveData<Status>,
            disposables: CompositeDisposable): LiveData<PagedList<Match>> {

        val factory = dao.getMatches(id)
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(40)
                .setPageSize(20)
                .setPrefetchDistance(5)
                .build()
        return LivePagedListBuilder(factory, config)
                .setBoundaryCallback(MatchBoundaryCallback(service, dao, status, disposables, id))
                .build()
    }

    fun refreshMatches(status: MutableLiveData<Status>, id: Long): Disposable {
        return dao.getMatchCount(id)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .flatMap { limit ->
                    if (limit != 0L) {
                        status.postValue(Status.LOADING)
                        service.getMatches(id, limit, 0)
                    } else {
                        Observable.never()
                    }
                }
                .flatMap { Observable.fromIterable(it) }
                .map {
                    it.accountId = id
                    it
                }.toList()
                .subscribe({
                    if (it.size != 0) {
                        dao.deleteMatches(id)
                        dao.insertMatches(it)
                    }
                    status.postValue(Status.SUCCESS)
                }, {
                    status.postValue(Status.ERROR)
                })
    }

    fun fetchMatchStats(
            match: MutableLiveData<MatchStats>,
            loading: MutableLiveData<Boolean>,
            matchId: Long): Disposable {
        loading.value = true

        return service.getMatch(matchId)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    match.postValue(it)
                    loading.postValue(false)
                }, {
                    loading.postValue(false)
                })
    }
}