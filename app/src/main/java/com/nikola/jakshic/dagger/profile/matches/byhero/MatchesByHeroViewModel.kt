package com.nikola.jakshic.dagger.profile.matches.byhero

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.profile.matches.MatchRepository
import com.nikola.jakshic.dagger.profile.matches.MatchUI
import kotlinx.coroutines.launch

class MatchesByHeroViewModel @ViewModelInject constructor(
    private val repository: MatchRepository
) : ScopedViewModel() {

    val matches: LiveData<PagedList<MatchUI>>
        get() = response.pagedList

    val status: LiveData<Status>
        get() = response.status

    private lateinit var response: PagedResponse<MatchUI>

    private var initialFetch = false

    fun initialFetch(accountId: Long, heroId: Int) {
        if (!initialFetch) {
            initialFetch = true
            response = repository.fetchMatchesByHero(accountId, heroId)
        }
    }

    fun retry() {
        launch {
            response.retry()
        }
    }

    fun refresh() = response.refresh()
}