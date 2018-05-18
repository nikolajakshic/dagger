package com.nikola.jakshic.dagger.ui.competitive

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nikola.jakshic.dagger.ui.Status
import com.nikola.jakshic.dagger.repository.CompetitiveRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CompetitiveViewModel @Inject constructor(
        private val repo: CompetitiveRepository) : ViewModel() {

    val list = repo.getCompetitiveLiveData()
    val status = MutableLiveData<Status>()
    private var initialFetch = false
    private val disposables = CompositeDisposable()
    private val onSuccess: () -> Unit = { status.value = Status.SUCCESS }
    private val onError: () -> Unit = { status.value = Status.ERROR }

    fun initialFetch() {
        if (!initialFetch) {
            initialFetch = true
            refreshData()
        }
    }

    fun refreshData() {
        status.value = Status.LOADING
        disposables.add(repo.fetchCompetitive(onSuccess, onError))
    }

    override fun onCleared() {
        disposables.clear()
    }
}