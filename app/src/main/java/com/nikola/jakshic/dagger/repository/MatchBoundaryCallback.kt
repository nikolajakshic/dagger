package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.data.local.MatchDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.vo.Match
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MatchBoundaryCallback(
        private val service: OpenDotaService,
        private val dao: MatchDao,
        private val disposables: CompositeDisposable,
        private val id: Long,
        private val onLoading: () -> Unit,
        private val onSuccess: () -> Unit,
        private val onError: () -> Unit) : PagedList.BoundaryCallback<Match>() {

    override fun onItemAtEndLoaded(itemAtEnd: Match) {
        onLoading()
        disposables.add(dao.getMatchCount(id)
                .subscribeOn(Schedulers.io())
                .toObservable()
                .flatMap { service.getMatches(id, 20, it) }
                .flatMap { Observable.fromIterable(it) }
                .map {
                    it.accountId = id   // response from the network doesn't contain any information
                    it           // about who played this matches, so we need to set this manually
                }
                .toList()
                .flatMapCompletable { Completable.fromAction { dao.insertMatches(it) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccess() }, { onError() }))
    }
}