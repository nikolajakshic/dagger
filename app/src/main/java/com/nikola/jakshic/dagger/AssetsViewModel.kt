package com.nikola.jakshic.dagger

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nikola.jakshic.dagger.common.Dispatchers
import com.nikola.jakshic.dagger.common.extensions.awaitNonCancellably
import com.nikola.jakshic.dagger.common.network.DaggerService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
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

private const val CONFIG_ITEMS_VERSION = "items_version"
private const val CONFIG_HEROES_VERSION = "heroes_version"

@Singleton
class AssetsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val network: DaggerService,
    private val database: Database,
    dispatchers: Dispatchers
) {
    init {
        val lifecycleOwner = ProcessLifecycleOwner.get()
        lifecycleOwner.lifecycleScope.launch(dispatchers.io) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (isActive) {
                    try {
                        val remoteConfig = network.getRemoteConfig().awaitNonCancellably()
                        handleItemsAssets(remoteConfig.itemsVersion)
                        handleHeroesAssets(remoteConfig.heroesVersion)
                        break
                    } catch (e: Exception) {
                        Timber.e(e)
                        delay(3000)
                    }
                }
            }
        }
    }

    private suspend fun handleItemsAssets(itemsVersion: Long) {
        val currentItemsVersion = database.localConfigQueries
            .selectConfigVersion(configName = CONFIG_ITEMS_VERSION)
            .executeAsOneOrNull() ?: 0
        if (currentItemsVersion == itemsVersion) {
            return
        }

        val assetsDirectory = File(context.filesDir, "assets")
        val itemsDirectory = File(assetsDirectory, "items")
        val currentItemsDirectory = File(itemsDirectory, "items_$itemsVersion")
        currentItemsDirectory.mkdirs()

        // If we previously failed to remove obsolete files, try again, to clear some space before we
        // download new files.
        for (directory in itemsDirectory.listFiles() ?: emptyArray()) {
            if (directory != currentItemsDirectory) {
                directory.deleteRecursively()
            }
        }

        val items = mutableListOf<Pair<Long, String>>()
        ZipInputStream(network.getItemsAssets().awaitNonCancellably().byteStream()).use { zip ->
            while (true) {
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
                configVersion = itemsVersion
            )
        }
        for (directory in itemsDirectory.listFiles() ?: emptyArray()) {
            if (directory != currentItemsDirectory) {
                directory.deleteRecursively()
            }
        }
    }

    private suspend fun handleHeroesAssets(heroesVersion: Long) {
        val currentHeroesVersion = database.localConfigQueries
            .selectConfigVersion(configName = CONFIG_HEROES_VERSION)
            .executeAsOneOrNull() ?: 0
        if (currentHeroesVersion == heroesVersion) {
            return
        }

        val assetsDirectory = File(context.filesDir, "assets")
        val heroesDirectory = File(assetsDirectory, "heroes")
        val currentHeroesDirectory = File(heroesDirectory, "heroes_$heroesVersion")
        currentHeroesDirectory.mkdirs()

        // If we previously failed to remove obsolete files, try again to clear some space before we
        // download new files.
        for (directory in heroesDirectory.listFiles() ?: emptyArray()) {
            if (directory != currentHeroesDirectory) {
                directory.deleteRecursively()
            }
        }

        val heroes = mutableListOf<Pair<Long, String>>()
        ZipInputStream(network.getHeroesAssets().awaitNonCancellably().byteStream()).use { zip ->
            while (true) {
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
                configVersion = heroesVersion
            )
        }

        for (directory in heroesDirectory.listFiles() ?: emptyArray()) {
            if (directory != currentHeroesDirectory) {
                directory.deleteRecursively()
            }
        }
    }
}
