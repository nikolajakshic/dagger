package com.nikola.jakshic.dagger.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nikola.jakshic.dagger.Status
import com.nikola.jakshic.dagger.repository.CompetitiveRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class CompetitiveViewModel @Inject constructor(
        private val repo: CompetitiveRepository
) : ViewModel() {

    private var initialFetch = false
    val list = repo.getCompetitiveFromDb()
    val status = MutableLiveData<Status>()
    private val disposables = CompositeDisposable()

    fun initialFetch() {
        if (!initialFetch) {
            initialFetch = true
            refreshData()
        }
    }

    fun refreshData() {
        disposables.add(repo.fetchCompetitive(status))
    }

    override fun onCleared() {
        disposables.clear()
    }
}