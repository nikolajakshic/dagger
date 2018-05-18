package com.nikola.jakshic.dagger.ui.profile.heroes

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Hero
import com.nikola.jakshic.dagger.repository.HeroRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HeroViewModel @Inject constructor(
        private val repository: HeroRepository) : ViewModel() {

    lateinit var list: LiveData<List<Hero>>
        private set
    val status = MutableLiveData<Status>()
    private val disposables = CompositeDisposable()
    private var initialFetch = false
    private val onSuccess: () -> Unit = { status.value = Status.SUCCESS }
    private val onError: () -> Unit = { status.value = Status.ERROR }

    fun initialFetch(id: Long){
        if (!initialFetch){
            initialFetch= true
            fetchHeroes(id)
            sortByGames(id)
        }
    }

    fun fetchHeroes(id: Long) {
        status.value = Status.LOADING
        disposables.add(repository.fetchHeroes(id, onSuccess, onError))
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

    override fun onCleared() {
        disposables.clear()
    }
}