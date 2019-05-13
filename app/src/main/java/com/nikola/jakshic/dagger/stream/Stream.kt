package com.nikola.jakshic.dagger.stream

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class StreamBox(@Json(name = "data") val stream: List<Stream>)

@JsonClass(generateAdapter = true)
data class Stream(
    @Json(name = "user_name") val userName: String,
    @Json(name = "title") val title: String,
    @Json(name = "viewer_count") val viewerCount: Long,
    @Json(name = "thumbnail_url") val thumbnailUrl: String
)