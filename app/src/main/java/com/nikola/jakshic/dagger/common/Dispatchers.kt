package com.nikola.jakshic.dagger.common

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Dispatchers @Inject constructor() {
    val io = Dispatchers.IO
    val main = Dispatchers.Main
}
