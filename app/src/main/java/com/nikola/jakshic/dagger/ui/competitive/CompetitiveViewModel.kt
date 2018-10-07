package com.nikola.jakshic.dagger.ui.competitive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nikola.jakshic.dagger.repository.CompetitiveRepository
import com.nikola.jakshic.dagger.ui.Status
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.cancelChildren
import javax.inject.Inject

class CompetitiveViewModel @Inject constructor(
        private val repo: CompetitiveRepository) : ViewModel() {

    val list = repo.getCompetitiveLiveData()
    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status
    private var initialFetch = false
    private val jobs = Job()
    private val onSuccess: () -> Unit = { _status.value = Status.SUCCESS }
    private val onError: () -> Unit = { _status.value = Status.ERROR }

    fun initialFetch() {
        if (!initialFetch) {
            initialFetch = true
            refreshData()
        }
    }

    fun refreshData() {
        _status.value = Status.LOADING
        repo.fetchCompetitive(jobs, onSuccess, onError)
    }

    override fun onCleared() {
        jobs.cancelChildren()
    }
}