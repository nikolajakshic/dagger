package com.nikola.jakshic.dagger.competitive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikola.jakshic.dagger.common.ScopedViewModel
import com.nikola.jakshic.dagger.common.Status
import kotlinx.coroutines.launch
import javax.inject.Inject

class CompetitiveViewModel @Inject constructor(
        private val repo: CompetitiveRepository) : ScopedViewModel() {

    val list = repo.getCompetitiveLiveData()

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
}