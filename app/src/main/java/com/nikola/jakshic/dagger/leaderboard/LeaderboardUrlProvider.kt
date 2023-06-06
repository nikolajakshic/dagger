package com.nikola.jakshic.dagger.leaderboard

import android.content.SharedPreferences
import androidx.core.content.edit
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.network.DaggerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import retrofit2.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY_LAST_URL = "last_url"
private const val KEY_LAST_CHECKED = "last_checked"

/**
 * Used for fetching Leaderboard's URL from the remote config file, so the next time www.dota2.com changes
 * their URL endpoint, we can just update the remote config file instead of updating Android app,
 * and publishing a new version.
 */
@Singleton
class LeaderboardUrlProvider @Inject constructor(
    applicationScope: CoroutineScope,
    dispatchers: Dispatchers,
    private val sharedPreferences: SharedPreferences,
    private val service: DaggerService,
) {
    private val url = flow {
        var lastUrl: String? = null
        try {
            lastUrl = sharedPreferences.getString(KEY_LAST_URL, null)
            val lastChecked = sharedPreferences.getLong(KEY_LAST_CHECKED, 0)
            if (System.currentTimeMillis() - lastChecked >= TimeUnit.HOURS.toMillis(3)) {
                lastUrl = service.getRemoteConfig().await().leaderboardUrl
                sharedPreferences.edit(commit = true) {
                    putString(KEY_LAST_URL, lastUrl)
                    putLong(KEY_LAST_CHECKED, System.currentTimeMillis())
                }
            }
        } catch (ignored: Exception) {
        }
        emit(lastUrl)
    }.flowOn(dispatchers.io)
        .shareIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 3000,
                replayExpirationMillis = 0,
            ),
            replay = 1,
        )

    suspend fun get() = url.first()
}
