package com.nikola.jakshic.dagger.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.nikola.jakshic.dagger.Status
import com.nikola.jakshic.dagger.data.local.CompetitiveDao
import com.nikola.jakshic.dagger.data.remote.OpenDotaService
import com.nikola.jakshic.dagger.model.Competitive
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompetitiveRepository @Inject constructor(
        private val dao: CompetitiveDao,
        private val service: OpenDotaService) {

    fun getCompetitiveFromDb(): LiveData<PagedList<Competitive>> {
        val factory = dao.getMatches()
        val config = PagedList.Config.Builder()
                .setInitialLoadSizeHint(80)
                .setPageSize(40)
                .setPrefetchDistance(15)
                .setEnablePlaceholders(false)
                .build()
        return LivePagedListBuilder(factory, config).build()
    }

    fun fetchCompetitive(status: MutableLiveData<Status>): Disposable {
        status.value = Status.LOADING

        return service.competitiveMatches
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    dao.insertMatches(it)
                    status.postValue(Status.SUCCESS)
                }, { _ ->
                    status.postValue(Status.ERROR)
                })
    }
}