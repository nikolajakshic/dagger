package com.nikola.jakshic.dagger.competitive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.common.paging.QueryPagingSourceFactory
import com.nikola.jakshic.dagger.common.sqldelight.Competitive
import com.nikola.jakshic.dagger.common.sqldelight.CompetitiveQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CompetitiveViewModel @Inject constructor(
    private val network: OpenDotaService,
    private val competitiveQueries: CompetitiveQueries,
    private val dispatchers: Dispatchers,
) : ViewModel() {
    val data = Pager(
        config = PagingConfig(
            pageSize = 40,
            prefetchDistance = 10,
            enablePlaceholders = false,
            initialLoadSize = 40,
        ),
        pagingSourceFactory = QueryPagingSourceFactory(
            competitiveQueries.countMatches(),
            competitiveQueries,
            dispatchers.io,
            competitiveQueries::getMatches,
        ),
    ).flow
        .map { pagingData -> pagingData.map { it.mapToUi() } }
        .cachedIn(viewModelScope)
        .flowOn(dispatchers.io)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchCompetitiveMatches()
    }

    fun fetchCompetitiveMatches() {
        viewModelScope.launch {
            if (_isLoading.value) {
                return@launch
            }
            _isLoading.value = true
            try {
                val matchesJson = network.getCompetitiveMatches()
                withContext(dispatchers.io) {
                    val matches = matchesJson.map {
                        Competitive(
                            match_id = it.matchId,
                            start_time = it.startTime,
                            duration = it.duration,
                            radiant_name = it.radiantName,
                            dire_name = it.direName,
                            league_name = it.leagueName,
                            radiant_score = it.radiantScore,
                            dire_score = it.direScore,
                            radiant_win = if (it.isRadiantWin) 1 else 0,
                        )
                    }
                    competitiveQueries.transaction {
                        matches.forEach(competitiveQueries::insert)
                    }
                }
            } catch (ignored: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }
}
