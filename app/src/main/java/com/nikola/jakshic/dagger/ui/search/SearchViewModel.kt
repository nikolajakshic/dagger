package com.nikola.jakshic.dagger.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.data.local.SearchHistoryDao
import com.nikola.jakshic.dagger.repository.PlayerRepository
import com.nikola.jakshic.dagger.ui.ScopedViewModel
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Player
import com.nikola.jakshic.dagger.vo.SearchHistory
import kotlinx.coroutines.*
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val dao: SearchHistoryDao,
        private val repository: PlayerRepository
) : ScopedViewModel() {

    private var job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _playerList = MutableLiveData<List<Player>>()
    val playerList: LiveData<List<Player>>
        get() = _playerList

    private val _historyList = MutableLiveData<List<SearchHistory>>()
    val historyList: LiveData<List<SearchHistory>>
        get() = _historyList

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val onSuccess: (List<Player>) -> Unit = {
        _status.value = Status.SUCCESS
        _playerList.value = it
    }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun getAllQueries() {
        launch {
            val list = withContext(Dispatchers.IO) { dao.getAllQueries() }
            _historyList.value = list
        }
    }

    fun getQueries(query: String) {
        launch {
            val list = withContext(Dispatchers.IO) { dao.getQuery(query) }
            _historyList.value = list
        }
    }

    fun saveQuery(item: SearchHistory) {
        launch {
            withContext(Dispatchers.IO) { dao.insertQuery(item) }
        }
    }

    fun fetchPlayers(name: String) {
        // Users can hit the search button multiple times
        // So we need to cancel previous call
        job.cancelChildren()

        scope.launch {
            _status.value = Status.LOADING
            repository.fetchPlayers(name, onSuccess, onError)
        }
    }

    fun clearList() {
        _playerList.value = ArrayList()
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}