package com.nikola.jakshic.dagger

import kotlinx.coroutines.experimental.asCoroutineDispatcher
import java.util.concurrent.Executors

object Dispatcher {
    val IO = Executors.newCachedThreadPool().asCoroutineDispatcher()
}