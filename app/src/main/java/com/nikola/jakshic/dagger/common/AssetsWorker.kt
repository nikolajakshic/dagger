package com.nikola.jakshic.dagger.common

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nikola.jakshic.dagger.Database
import com.nikola.jakshic.dagger.common.network.DaggerService
import com.nikola.jakshic.dagger.leaderboard.RemoteConfig
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.util.zip.ZipInputStream

private const val CONFIG_ITEMS_VERSION = "items_version"
private const val CONFIG_HEROES_VERSION = "heroes_version"

@HiltWorker
class AssetsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val network: DaggerService,
    private val database: Database,
    private val dispatchers: Dispatchers
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result = withContext(dispatchers.io) {
        val remoteConfig = withRetry { network.getRemoteConfig() }

        var errorHappened = false
        try {
            withRetry { handleItemsAssets(remoteConfig) }
        } catch (ignored: Exception) {
            errorHappened = true
        }
        try {
            withRetry { handleHeroesAssets(remoteConfig) }
        } catch (ignored: Exception) {
            errorHappened = true
        }

        if (errorHappened) {
            return@withContext Result.retry()
        }
        return@withContext Result.success()
    }

    private suspend fun handleItemsAssets(remoteConfig: RemoteConfig) = coroutineScope {
        val currentItemsVersion = database.localConfigQueries
            .selectConfigVersion(configName = CONFIG_ITEMS_VERSION)
            .executeAsOneOrNull() ?: 0
        if (currentItemsVersion == remoteConfig.itemsVersion) {
            return@coroutineScope
        }

        val assetsDirectory = File(applicationContext.filesDir, "assets")
        val itemsDirectory = File(assetsDirectory, "items")
        val currentItemsDirectory = File(itemsDirectory, "items_${remoteConfig.itemsVersion}")
        currentItemsDirectory.mkdirs()

        val items = mutableListOf<Pair<Long, String>>()
        ZipInputStream(network.getItemsAssets().byteStream()).use { zip ->
            while (isActive) {
                val zipEntry = zip.nextEntry ?: break
                val file = File(currentItemsDirectory, zipEntry.name)
                file.sink().buffer().use { sink ->
                    sink.writeAll(zip.source().buffer())
                }
                items += Pair(file.nameWithoutExtension.toLong(), file.absolutePath)
            }
        }

        database.transaction {
            database.itemAssetQueries.deleteAll()
            items.forEach { (itemId, imagePath) ->
                database.itemAssetQueries.insert(itemId, imagePath)
            }
            database.localConfigQueries.insert(
                configName = CONFIG_ITEMS_VERSION,
                configVersion = remoteConfig.itemsVersion
            )
        }

        for (directory in itemsDirectory.listFiles()) {
            if (directory != currentItemsDirectory) {
                directory.deleteRecursively()
            }
        }
    }

    private suspend fun handleHeroesAssets(remoteConfig: RemoteConfig) = coroutineScope {
        val currentHeroesVersion = database.localConfigQueries
            .selectConfigVersion(configName = CONFIG_HEROES_VERSION)
            .executeAsOneOrNull() ?: 0
        if (currentHeroesVersion == remoteConfig.heroesVersion) {
            return@coroutineScope
        }

        val assetsDirectory = File(applicationContext.filesDir, "assets")
        val heroesDirectory = File(assetsDirectory, "heroes")
        val currentHeroesDirectory = File(heroesDirectory, "heroes_${remoteConfig.heroesVersion}")
        currentHeroesDirectory.mkdirs()

        val heroes = mutableListOf<Pair<Long, String>>()
        ZipInputStream(network.getHeroesAssets().byteStream()).use { zip ->
            while (isActive) {
                val zipEntry = zip.nextEntry ?: break
                val file = File(currentHeroesDirectory, zipEntry.name)
                file.sink().buffer().use { sink ->
                    sink.writeAll(zip.source().buffer())
                }
                heroes += Pair(file.nameWithoutExtension.toLong(), file.absolutePath)
            }
        }

        database.transaction {
            database.heroAssetQueries.deleteAll()
            heroes.forEach { (heroId, imagePath) ->
                database.heroAssetQueries.insert(heroId, imagePath)
            }
            database.localConfigQueries.insert(
                configName = CONFIG_HEROES_VERSION,
                configVersion = remoteConfig.heroesVersion
            )
        }

        for (directory in heroesDirectory.listFiles()) {
            if (directory != currentHeroesDirectory) {
                directory.deleteRecursively()
            }
        }
    }
}
