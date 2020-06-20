package com.nikola.jakshic.dagger.profile.heroes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HeroViewModel @Inject constructor(
    private val repository: HeroRepository
) : ScopedViewModel() {

    private val _list = MutableLiveData<List<HeroUI>>()
    val list: LiveData<List<HeroUI>>
        get() = _list

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private var initialFetch = false

    private val onSuccess: () -> Unit = { _status.value = Status.SUCCESS }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun initialFetch(id: Long) {
        if (!initialFetch) {
            initialFetch = true
            fetchHeroes(id)
            sortByGames(id)
        }
    }

    fun fetchHeroes(id: Long) {
        launch {
            _status.value = Status.LOADING
            repository.fetchHeroes(id, onSuccess, onError)
        }
    }

    fun sortByGames(id: Long) {
        launch {
            repository.getHeroesFlowByGames(id)
                .collectLatest { _list.value = it }
        }
    }

    fun sortByWinRate(id: Long) {
        launch {
            repository.getHeroesFlowByWinrate(id)
                .collectLatest { _list.value = it }
        }
    }

    fun sortByWins(id: Long) {
        launch {
            repository.getHeroesFlowByWins(id)
                .collectLatest { _list.value = it }
        }
    }

    fun sortByLosses(id: Long) {
        launch {
            repository.getHeroesFlowByLosses(id)
                .collectLatest { _list.value = it }
        }
    }
}