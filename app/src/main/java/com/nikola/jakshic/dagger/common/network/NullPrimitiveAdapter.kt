package com.nikola.jakshic.dagger.common.network

import com.squareup.moshi.FromJson
import kotlin.annotation.AnnotationRetention.*

class NullPrimitiveAdapter {
    @FromJson
    fun fromJson(@Nullable value: Int?): Int = value ?: 0

    @FromJson
    fun fromJson(@Nullable value: Long?): Long = value ?: 0

    @FromJson
    fun fromJson(@Nullable value: Float?): Float = value ?: 0f

    @FromJson
    fun fromJson(@Nullable value: Double?): Double = value ?: 0.0

    @FromJson
    fun fromJson(@Nullable value: Boolean?): Boolean = value ?: false
}

@Retention(RUNTIME)
annotation class Nullable