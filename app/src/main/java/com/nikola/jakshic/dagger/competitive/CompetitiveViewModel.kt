package com.nikola.jakshic.dagger.competitive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.paging.QueryDataSourceFactory
import com.nikola.jakshic.dagger.common.sqldelight.Competitive
import com.nikola.jakshic.dagger.common.sqldelight.CompetitiveQueries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitiveViewModel @Inject constructor(
    private val repo: CompetitiveRepository,
    competitiveQueries: CompetitiveQueries
) : ScopedViewModel() {

    // Workaround for leaky QueryDataSource, store the reference so we can release the resources.
    private val factory = QueryDataSourceFactory(
        queryProvider = competitiveQueries::getMatches,
        countQuery = competitiveQueries.countMatches(),
        transacter = competitiveQueries
    )

    val list = repo.getCompetitiveLiveData(factory.map(Competitive::mapToUi))

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private val onSuccess: () -> Unit = { _status.value = Status.SUCCESS }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    init {
        refreshData()
    }

    fun refreshData() {
        launch {
            _status.value = Status.LOADING
            repo.fetchCompetitive(onSuccess, onError)
        }
    }

    override fun onCleared() {
        super.onCleared()
        factory.invalidate()
    }
}