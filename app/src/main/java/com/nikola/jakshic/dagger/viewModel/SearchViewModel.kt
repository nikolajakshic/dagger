package com.nikola.jakshic.dagger.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nikola.jakshic.dagger.Status
import com.nikola.jakshic.dagger.data.local.SearchHistoryDao
import com.nikola.jakshic.dagger.model.Player
import com.nikola.jakshic.dagger.model.SearchHistory
import com.nikola.jakshic.dagger.repository.PlayerRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val dao: SearchHistoryDao,
        private val repository: PlayerRepository
) : ViewModel() {

    val playerList = MutableLiveData<List<Player>>()
    val historyList = MutableLiveData<List<SearchHistory>>()
    val status = MutableLiveData<Status>()
    private val disposables = CompositeDisposable()

    fun getAllQueries() {
        disposables.add(Single
                .fromCallable(dao::getAllQueries)
                .subscribeOn(Schedulers.io())
                .subscribe(historyList::postValue))
    }

    fun getQueries(query: String) {
        disposables.add(Single
                .fromCallable { dao.getQuery(query) }
                .subscribeOn(Schedulers.io())
                .subscribe(historyList::postValue))
    }

    fun saveQuery(item: SearchHistory) {
        disposables.add(Completable
                .fromAction { dao.insertQuery(item) }
                .subscribeOn(Schedulers.io())
                .subscribe())
    }

    fun fetchPlayers(name: String) {
        disposables.add(repository.fetchPlayers(playerList, status, name))
    }

    fun clearList() {
        playerList.value = ArrayList()
    }

    override fun onCleared() {
        disposables.clear()
    }
}