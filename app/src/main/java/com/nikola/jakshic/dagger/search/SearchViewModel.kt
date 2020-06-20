package com.nikola.jakshic.dagger.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.sqldelight.SearchHistoryQueries
import com.nikola.jakshic.dagger.common.sqldelight.Search_history
import com.nikola.jakshic.dagger.profile.PlayerJson
import com.nikola.jakshic.dagger.profile.PlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchHistoryQueries: SearchHistoryQueries,
    private val repository: PlayerRepository
) : ScopedViewModel() {

    private var job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val _playerList = MutableLiveData<List<PlayerJson>>()
    val playerList: LiveData<List<PlayerJson>>
        get() = _playerList

    private val _historyList = MutableLiveData<List<Search_history>>()
    val historyList: LiveData<List<Search_history>>
        get() = _historyList

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val onSuccess: (List<PlayerJson>) -> Unit = {
        _status.value = Status.SUCCESS
        _playerList.value = it
    }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun getAllQueries() {
        launch {
            val list = withContext(Dispatchers.IO) { searchHistoryQueries.selectAll().executeAsList() }
            _historyList.value = list
        }
    }

    fun getQueries(query: String) {
        launch {
            val list = withContext(Dispatchers.IO) { searchHistoryQueries.selectAllByQuery(query).executeAsList() }
            _historyList.value = list
        }
    }

    fun saveQuery(item: SearchHistory) {
        launch {
            withContext(Dispatchers.IO) {
                // ID is auto-incrementing, the value we passed here is irrelevant.
                searchHistoryQueries.insert(Search_history(-1, item.query))
            }
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