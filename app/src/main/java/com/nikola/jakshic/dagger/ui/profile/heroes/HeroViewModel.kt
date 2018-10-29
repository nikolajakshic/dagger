package com.nikola.jakshic.dagger.ui.profile.heroes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.repository.HeroRepository
import com.nikola.jakshic.dagger.ui.ScopedViewModel
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Hero
import kotlinx.coroutines.launch
import javax.inject.Inject

class HeroViewModel @Inject constructor(
        private val repository: HeroRepository) : ScopedViewModel() {

    lateinit var list: LiveData<List<Hero>>
        private set

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
        list = repository.getHeroesLiveDataByGames(id)
    }

    fun sortByWinRate(id: Long) {
        list = repository.getHeroesLiveDataByWinrate(id)
    }

    fun sortByWins(id: Long) {
        list = repository.getHeroesLiveDataByWins(id)
    }

    fun sortByLosses(id: Long) {
        list = repository.getHeroesLiveDataByLosses(id)
    }
}