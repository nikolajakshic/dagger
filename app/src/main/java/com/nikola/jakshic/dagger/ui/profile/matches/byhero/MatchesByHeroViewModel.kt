package com.nikola.jakshic.dagger.ui.profile.matches.byhero

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.repository.MatchRepository
import com.nikola.jakshic.dagger.ui.ScopedViewModel
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.vo.Match
import javax.inject.Inject

class MatchesByHeroViewModel @Inject constructor(private val repository: MatchRepository) : ScopedViewModel() {

    val matches: LiveData<PagedList<Match>>
        get() = response.pagedList

    val status: LiveData<Status>
        get() = response.status

    private lateinit var response: PagedResponse<Match>

    private var initialFetch = false

    fun initialFetch(accountId: Long, heroId: Int) {
        if (!initialFetch) {
            initialFetch = true

            response = repository.fetchMatchesByHero(accountId, heroId)
        }
    }

    fun retry() = response.retry()

    fun refresh() = response.refresh()
}