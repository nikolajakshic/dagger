package com.nikola.jakshic.dagger.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikola.jakshic.dagger.Dispatcher.IO
import com.nikola.jakshic.dagger.data.local.SearchHistoryDao
import com.nikola.jakshic.dagger.repository.PlayerRepository
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Player
import com.nikola.jakshic.dagger.vo.SearchHistory
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.cancelChildren
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val dao: SearchHistoryDao,
        private val repository: PlayerRepository
) : ViewModel() {

    val playerList = MutableLiveData<List<Player>>()
    val historyList = MutableLiveData<List<SearchHistory>>()
    val status = MutableLiveData<Status>()
    private val jobs = Job()
    private val onSuccess: (List<Player>) -> Unit = {
        status.value = Status.SUCCESS
        playerList.value = it
    }
    private val onError: () -> Unit = { status.value = Status.ERROR }

    fun getAllQueries() {
        launch(UI) {
            val list = withContext(IO) { dao.getAllQueries() }
            historyList.value = list
        }
    }

    fun getQueries(query: String) {
        launch(UI) {
            val list = withContext(IO) { dao.getQuery(query) }
            historyList.value = list
        }
    }

    fun saveQuery(item: SearchHistory) {
        launch(UI) {
            withContext(IO) { dao.insertQuery(item) }
        }
    }

    fun fetchPlayers(name: String) {
        status.value = Status.LOADING
        // Users can hit the search button multiple times
        // So we need to cancel previous call
        jobs.cancelChildren()
        repository.fetchPlayers(jobs, name, onSuccess, onError)
    }

    fun clearList() {
        playerList.value = ArrayList()
    }

    override fun onCleared() {
        jobs.cancelChildren()
    }
}