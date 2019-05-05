package com.nikola.jakshic.dagger.profile.matches.byhero

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.nikola.jakshic.dagger.profile.matches.MatchRepository
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.profile.matches.Match
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MatchesByHeroViewModel @Inject constructor(private val repository: MatchRepository) : ScopedViewModel() {

    val matches: LiveData<PagedList<Match>>
        get() = response.pagedList

    val status: LiveData<Status>
        get() = response.status

    private lateinit var response: PagedResponse<Match>

    private var initialFetch = false

    fun initialFetch(accountId: Long, heroId: Int) {
        runBlocking {
            launch {
                if (!initialFetch) {
                    initialFetch = true

                    response = repository.fetchMatchesByHero(accountId, heroId)
                }
            }
        }
    }

    fun retry() {
        launch {
            response.retry()
        }
    }

    fun refresh() = response.refresh()
}