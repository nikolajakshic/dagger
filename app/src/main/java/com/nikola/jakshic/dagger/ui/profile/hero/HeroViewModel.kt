package com.nikola.jakshic.dagger.ui.profile.hero

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nikola.jakshic.dagger.Status
import com.nikola.jakshic.dagger.model.Hero
import com.nikola.jakshic.dagger.repository.HeroRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HeroViewModel @Inject constructor(private val repository: HeroRepository) : ViewModel() {

    lateinit var list: LiveData<List<Hero>>
        private set
    val status = MutableLiveData<Status>()
    private val disposables = CompositeDisposable()
    private var initialFetch = false

    fun initialFetch(id: Long){
        if (!initialFetch){
            initialFetch= true
            fetchHeroes(id)
            sortByGames(id)
        }
    }

    fun fetchHeroes(id: Long) {
        disposables.add(repository.fetchHeroes(status, id))
    }

    fun sortByGames(id: Long) {
        list = repository.sortByGames(id)
    }

    fun sortByWinRate(id: Long) {
        list = repository.sortByWinRate(id)
    }

    fun sortByWins(id: Long) {
        list = repository.sortByWins(id)
    }

    fun sortByLosses(id: Long) {
        list = repository.sortByLosses(id)
    }

    override fun onCleared() {
        disposables.clear()
    }
}