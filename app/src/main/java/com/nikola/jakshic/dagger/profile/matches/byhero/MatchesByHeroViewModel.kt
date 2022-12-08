package com.nikola.jakshic.dagger.profile.matches.byhero

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.profile.matches.MatchRepository
import com.nikola.jakshic.dagger.profile.matches.MatchUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchesByHeroViewModel @Inject constructor(
    private val repository: MatchRepository
) : ViewModel() {
    val matches: LiveData<PagedList<MatchUI>>
        get() = response.pagedList

    val status: LiveData<Status>
        get() = response.status

    private lateinit var response: PagedResponse<MatchUI>

    private var initialFetch = false

    fun initialFetch(accountId: Long, heroId: Long) {
        if (!initialFetch) {
            initialFetch = true
            response = repository.fetchMatchesByHero(accountId, heroId)
        }
    }

    fun retry() {
        viewModelScope.launch {
            response.retry()
        }
    }

    fun refresh() = response.refresh()
}
