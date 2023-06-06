package com.nikola.jakshic.dagger.competitive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.nikola.jakshic.dagger.common.paging.QueryDataSourceFactory
import com.nikola.jakshic.dagger.common.sqldelight.Competitive
import com.nikola.jakshic.dagger.common.sqldelight.CompetitiveQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitiveViewModel @Inject constructor(
    private val repo: CompetitiveRepository,
    competitiveQueries: CompetitiveQueries,
) : ViewModel() {
    // Workaround for leaky QueryDataSource, store the reference so we can release the resources.
    private val factory = QueryDataSourceFactory(
        queryProvider = competitiveQueries::getMatches,
        countQuery = competitiveQueries.countMatches(),
        transacter = competitiveQueries,
    )

    val list = repo.getCompetitiveLiveData(factory.map(Competitive::mapToUi)).asFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchCompetitiveMatches()
    }

    fun fetchCompetitiveMatches() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repo.fetchCompetitive()
            } catch (ignored: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        factory.invalidate()
    }
}
