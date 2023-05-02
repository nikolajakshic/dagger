package com.nikola.jakshic.dagger.assets

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nikola.jakshic.dagger.Database
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.extensions.awaitNonCancellably
import com.nikola.jakshic.dagger.common.network.DaggerService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okio.buffer
import okio.sink
import okio.source
import timber.log.Timber
import java.io.File
import java.util.zip.ZipInputStream
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

private const val CONFIG_ITEMS_VERSION = "items_version"
private const val CONFIG_HEROES_VERSION = "heroes_version"

@Singleton
class AssetsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val network: DaggerService,
    private val database: Database,
    dispatchers: Dispatchers
) : LifecycleOwner by ProcessLifecycleOwner.get(),
    CoroutineScope by ProcessLifecycleOwner.get().lifecycleScope {
    private val _statusMessage = MutableSharedFlow<String?>(
        replay = 1,
        extraBufferCapacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val statusMessage: Flow<String?> = _statusMessage

    init {
        launch(dispatchers.io) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // It is important not to have cancelable stuff inside this coroutine,
                // so all the steps, that are already queued, can complete uninterruptedly.
                // P.S. delay(3000) doesn't matter because it is inside catch block, we are done anyway.
                while (isActive) {
                    try {
                        val remoteConfig = network.getRemoteConfig().awaitNonCancellably()
                        val localItemsVersion = database.localConfigQueries
                            .selectConfigVersion(CONFIG_ITEMS_VERSION)
                            .executeAsOneOrNull() ?: -1
                        val localHeroesVersion = database.localConfigQueries
                            .selectConfigVersion(CONFIG_HEROES_VERSION)
                            .executeAsOneOrNull() ?: -1
                        val itemsHandled = handleItemsAssets(
                            localItemsVersion,
                            remoteConfig.itemsVersion
                        )
                        val heroesHandled = handleHeroesAssets(
                            localHeroesVersion,
                            remoteConfig.heroesVersion
                        )
                        if (itemsHandled || heroesHandled) {
                            _statusMessage.tryEmit("Assets downloaded")
                        }
                        break
                    } catch (e: Exception) {
                        Timber.e(e)
                        delay(3000)
                    } finally {
                        _statusMessage.tryEmit(null)
                    }
                }
            }
        }
    }

    private suspend fun handleItemsAssets(
        localItemsVersion: Long,
        remoteItemsVersion: Long
    ): Boolean {
        if (localItemsVersion == remoteItemsVersion) {
            return false
        }

        if (localItemsVersion == -1L) {
            _statusMessage.tryEmit("Downloading items: 0%")
        }

        val assetsDirectory = File(context.filesDir, "assets")
        val itemsDirectory = File(assetsDirectory, "items")
        val currentItemsDirectory = File(itemsDirectory, "items_$remoteItemsVersion")
        currentItemsDirectory.mkdirs()

        // If we previously failed to remove obsolete files, try again, to clear some space before we
        // download new files.
        for (directory in itemsDirectory.listFiles() ?: emptyArray()) {
            if (directory != currentItemsDirectory) {
                directory.deleteRecursively()
            }
        }

        val items = mutableListOf<Pair<Long, String>>()
        val responseBody = network.getItemsAssets().awaitNonCancellably()
        val contentLength = responseBody.contentLength().toFloat()
        ZipInputStream(responseBody.byteStream()).use { zip ->
            var totalBytesRead = 0L
            while (true) {
                val zipEntry = zip.nextEntry ?: break
                val file = File(currentItemsDirectory, zipEntry.name)
                file.sink().buffer().use { sink ->
                    while (true) {
                        val readCount = zip.source().read(sink.buffer, 8192)
                        if (readCount == -1L) break
                        sink.emitCompleteSegments()
                    }
                    totalBytesRead += zipEntry.compressedSize
                    if (localItemsVersion == -1L) {
                        val progress = min((95 * (totalBytesRead / contentLength)).toLong(), 95)
                        _statusMessage.tryEmit("Downloading items: $progress%")
                    }
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
                configVersion = remoteItemsVersion
            )
        }

        for (directory in itemsDirectory.listFiles() ?: emptyArray()) {
            if (directory != currentItemsDirectory) {
                directory.deleteRecursively()
            }
        }

        if (localItemsVersion == -1L) {
            _statusMessage.tryEmit("Downloading items: 100%")
        }

        return true
    }

    private suspend fun handleHeroesAssets(
        localHeroesVersion: Long,
        remoteHeroesVersion: Long
    ): Boolean {
        if (localHeroesVersion == remoteHeroesVersion) {
            return false
        }

        if (localHeroesVersion == -1L) {
            _statusMessage.tryEmit("Downloading heroes: 0%")
        }

        val assetsDirectory = File(context.filesDir, "assets")
        val heroesDirectory = File(assetsDirectory, "heroes")
        val currentHeroesDirectory = File(heroesDirectory, "heroes_$remoteHeroesVersion")
        currentHeroesDirectory.mkdirs()

        // If we previously failed to remove obsolete files, try again to clear some space before we
        // download new files.
        for (directory in heroesDirectory.listFiles() ?: emptyArray()) {
            if (directory != currentHeroesDirectory) {
                directory.deleteRecursively()
            }
        }

        val heroes = mutableListOf<Pair<Long, String>>()
        val responseBody = network.getHeroesAssets().awaitNonCancellably()
        val contentLength = responseBody.contentLength().toFloat()
        ZipInputStream(responseBody.byteStream()).use { zip ->
            var totalBytesRead = 0L
            while (true) {
                val zipEntry = zip.nextEntry ?: break
                val file = File(currentHeroesDirectory, zipEntry.name)
                file.sink().buffer().use { sink ->
                    while (true) {
                        val readCount = zip.source().read(sink.buffer, 8192)
                        if (readCount == -1L) break
                        sink.emitCompleteSegments()
                    }
                    totalBytesRead += zipEntry.compressedSize
                    if (localHeroesVersion == -1L) {
                        val progress = min((95 * (totalBytesRead / contentLength)).toLong(), 95)
                        _statusMessage.tryEmit("Downloading heroes: $progress%")
                    }
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
                configVersion = remoteHeroesVersion
            )
        }

        for (directory in heroesDirectory.listFiles() ?: emptyArray()) {
            if (directory != currentHeroesDirectory) {
                directory.deleteRecursively()
            }
        }

        if (localHeroesVersion == -1L) {
            _statusMessage.tryEmit("Downloading heroes: 100%")
        }

        return true
    }
}
