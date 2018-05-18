package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.nikola.jakshic.dagger.data.local.CompetitiveDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.vo.Competitive
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompetitiveRepository @Inject constructor(
        private val dao: CompetitiveDao,
        private val service: OpenDotaService) {

    /**
     * Constructs the [LiveData] which emits every time
     * the requested data in the database has changed
     */
    fun getCompetitiveLiveData(): LiveData<PagedList<Competitive>> {
        val factory = dao.getMatches()
        val config = PagedList.Config.Builder()
                .setInitialLoadSizeHint(80)
                .setPageSize(40)
                .setPrefetchDistance(15)
                .setEnablePlaceholders(false)
                .build()
        return LivePagedListBuilder(factory, config).build()
    }

    /**
     * Fetches the matches from the network and inserts them into database.
     *
     * Whenever the database is updated, the observers of [LiveData]
     * returned by [getCompetitiveLiveData] are notified.
     *
     * @param onSuccess called on main thread
     * @param onError called on main thread
     */
    fun fetchCompetitive(onSuccess: () -> Unit, onError: () -> Unit): Disposable {
        return service.getCompetitiveMatches()
                .subscribeOn(Schedulers.io())
                .concatMapCompletable { Completable.fromAction { dao.insertMatches(it) } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccess() }, { onError() })
    }
}