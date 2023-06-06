package com.nikola.jakshic.dagger.profile.matches.byhero

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.OpenDotaService
import com.nikola.jakshic.dagger.profile.matches.MatchUI

class MatchesByHeroDataSourceFactory(
    private val accountId: Long,
    private val heroId: Long,
    private val service: OpenDotaService,
    private val dispatchers: Dispatchers,
) : DataSource.Factory<Int, MatchUI>() {

    private val _sourceLiveData = MutableLiveData<MatchesByHeroDataSource>()
    val sourceLiveData: LiveData<MatchesByHeroDataSource>
        get() = _sourceLiveData

    override fun create(): DataSource<Int, MatchUI> {
        val source = MatchesByHeroDataSource(accountId, heroId, service, dispatchers)
        _sourceLiveData.postValue(source)
        return source
    }
}
