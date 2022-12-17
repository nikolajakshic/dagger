package com.nikola.jakshic.dagger.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.sqldelight.SearchHistoryQueries
import com.nikola.jakshic.dagger.profile.PlayerRepository
import com.nikola.jakshic.dagger.profile.PlayerUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// TODO Inject SavedStateHandle to handle system-initiated process death.
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchHistoryQueries: SearchHistoryQueries,
    private val repository: PlayerRepository,
    private val dispatchers: Dispatchers
) : ViewModel() {
    private var job = Job()
    private val scope = CoroutineScope(dispatchers.main + job)

    private val _playerList = MutableLiveData<List<PlayerUI>>()
    val playerList: LiveData<List<PlayerUI>>
        get() = _playerList

    private val _historyList = MutableLiveData<List<SearchHistoryUI>>()
    val historyList: LiveData<List<SearchHistoryUI>>
        get() = _historyList

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val onSuccess: (List<PlayerUI>) -> Unit = {
        _status.value = Status.SUCCESS
        _playerList.value = it
    }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun getAllQueries() {
        viewModelScope.launch {
            val list = withContext(dispatchers.io) {
                searchHistoryQueries.selectAll()
                    .executeAsList()
                    .map { SearchHistoryUI(it) }
            }
            _historyList.value = list
        }
    }

    fun getQueries(query: String) {
        viewModelScope.launch {
            val list = withContext(dispatchers.io) {
                searchHistoryQueries.selectAllByQuery(query)
                    .executeAsList()
                    .map { SearchHistoryUI(it) }
            }
            _historyList.value = list
        }
    }

    fun saveQuery(query: String) {
        viewModelScope.launch {
            withContext(dispatchers.io) {
                searchHistoryQueries.insert(query)
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

    fun deleteSearchHistory() {
        viewModelScope.launch {
            withContext(dispatchers.io) { searchHistoryQueries.deleteAll() }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
