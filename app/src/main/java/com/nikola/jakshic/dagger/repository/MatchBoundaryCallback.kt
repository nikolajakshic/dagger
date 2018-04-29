package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.nikola.jakshic.dagger.Status
import com.nikola.jakshic.dagger.data.local.MatchDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.model.match.Match
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MatchBoundaryCallback(
        private val service: OpenDotaService,
        private val dao: MatchDao,
        private val status: MutableLiveData<Status>,
        private val disposables: CompositeDisposable,
        private val id: Long
) : PagedList.BoundaryCallback<Match>(){

    override fun onZeroItemsLoaded() {
        status.value = Status.LOADING
        disposables.add(service.getMatches(id, 20, 0)
                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it) }
                .map {
                    it.accountId = id
                    it
                }
                .toList()
                .subscribe({
                    dao.insertMatches(it)
                    status.postValue(Status.SUCCESS)
                },{
                    status.postValue(Status.ERROR)
                }))
    }

    override fun onItemAtEndLoaded(itemAtEnd: Match) {
        status.value = Status.LOADING
        disposables.add(dao.getMatchCount(id)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .flatMap {
                    service.getMatches(id, 20, it) }
                .flatMap { Observable.fromIterable(it) }
                .map {
                    it.accountId = id
                    it
                }
                .toList()
                .subscribe({
                    dao.insertMatches(it)
                    status.postValue(Status.SUCCESS)
                },{
                    status.postValue(Status.ERROR)
                }))
    }
}