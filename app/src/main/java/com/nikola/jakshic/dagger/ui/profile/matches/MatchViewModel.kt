package com.nikola.jakshic.dagger.ui.profile.matches

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Match
import com.nikola.jakshic.dagger.repository.MatchRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MatchViewModel @Inject constructor(private val repository: MatchRepository) : ViewModel() {

    lateinit var list: LiveData<PagedList<Match>>
        private set
    val status = MutableLiveData<Status>()
    private var initialFetch = false
    private val disposables = CompositeDisposable()

    fun initialFetch(id: Long){
        if(!initialFetch){
            initialFetch = true
            list = repository.getMatchesFromDb(id,status, disposables)
            fetchMatches(id)
        }
    }

    fun fetchMatches(id: Long){
        disposables.add(repository.refreshMatches(status, id))
    }

    override fun onCleared() {
        disposables.clear()
    }
}