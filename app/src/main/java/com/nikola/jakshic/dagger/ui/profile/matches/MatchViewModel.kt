package com.nikola.jakshic.dagger.ui.profile.matches

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.nikola.jakshic.dagger.Status
import com.nikola.jakshic.dagger.model.match.Match
import com.nikola.jakshic.dagger.repository.MatchRepository
import javax.inject.Inject

class MatchViewModel @Inject constructor(private val repository: MatchRepository) : ViewModel() {

    lateinit var list: LiveData<PagedList<Match>>
        private set
    val status = MutableLiveData<Status>()
    private var initialFetch = false

    fun initialFetch(id: Long){
        if(!initialFetch){
            initialFetch = true
            list = repository.getMatches(status, id)
            repository.refreshMatches(status, id)
        }
    }

    fun fetchMatches(id: Long){
        repository.refreshMatches(status, id)
    }
}