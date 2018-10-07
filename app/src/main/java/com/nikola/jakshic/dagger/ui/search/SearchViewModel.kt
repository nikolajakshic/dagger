package com.nikola.jakshic.dagger.ui.search

import androidx.lifecycle.LiveData
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

    private val _playerList = MutableLiveData<List<Player>>()
    val playerList: LiveData<List<Player>>
        get() = _playerList

    private val _historyList = MutableLiveData<List<SearchHistory>>()
    val historyList: LiveData<List<SearchHistory>>
        get() = _historyList

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val jobs = Job()
    private val onSuccess: (List<Player>) -> Unit = {
        _status.value = Status.SUCCESS
        _playerList.value = it
    }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun getAllQueries() {
        launch(UI) {
            val list = withContext(IO) { dao.getAllQueries() }
            _historyList.value = list
        }
    }

    fun getQueries(query: String) {
        launch(UI) {
            val list = withContext(IO) { dao.getQuery(query) }
            _historyList.value = list
        }
    }

    fun saveQuery(item: SearchHistory) {
        launch(UI) {
            withContext(IO) { dao.insertQuery(item) }
        }
    }

    fun fetchPlayers(name: String) {
        _status.value = Status.LOADING
        // Users can hit the search button multiple times
        // So we need to cancel previous call
        jobs.cancelChildren()
        repository.fetchPlayers(jobs, name, onSuccess, onError)
    }

    fun clearList() {
        _playerList.value = ArrayList()
    }

    override fun onCleared() {
        jobs.cancelChildren()
    }
}