package com.nikola.jakshic.dagger.common.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TwitchAuthentication(@Json(name = "access_token") val token: String)