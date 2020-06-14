package com.nikola.jakshic.dagger.profile.matches.byhero

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.nikola.jakshic.dagger.common.Status
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.profile.matches.MatchJson
import com.nikola.jakshic.dagger.profile.matches.MatchUI
import com.nikola.jakshic.dagger.profile.matches.mapToUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MatchesByHeroDataSource(
    private val accountId: Long,
    private val heroId: Int,
    private val service: OpenDotaService
) : PositionalDataSource<MatchUI>() {

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private var retry: (() -> Any)? = null
        get() {
            val tmp = field
            field = null
            return tmp
        }

    suspend fun retry() {
        withContext(Dispatchers.IO) {
            retry?.invoke()
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<MatchUI>) {
        runBlocking {
            try {
                _status.postValue(Status.LOADING)
                val matches = withContext(Dispatchers.IO) {
                    service.getMatchesByHero(accountId, heroId, 60, 0).map(MatchJson::mapToUi)
                }
                callback.onResult(matches, 0)
                _status.postValue(Status.SUCCESS)
                retry = null
            } catch (e: Exception) {
                retry = { invalidate() }
                _status.postValue(Status.ERROR)
            }
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<MatchUI>) {
        runBlocking {
            try {
                _status.postValue(Status.LOADING)
                val offset = params.startPosition
                val matches = withContext(Dispatchers.IO) {
                    service.getMatchesByHero(accountId, heroId, 20, offset).map(MatchJson::mapToUi)
                }
                callback.onResult(matches)
                _status.postValue(Status.SUCCESS)
                retry = null
            } catch (e: Exception) {
                retry = { loadRange(params, callback) }
                _status.postValue(Status.ERROR)
            }
        }
    }
}