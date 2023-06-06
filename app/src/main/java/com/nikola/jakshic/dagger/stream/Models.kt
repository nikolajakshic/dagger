package com.nikola.jakshic.dagger.stream

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class StreamWrapperJson(@Json(name = "data") val stream: List<StreamJson>)

@JsonClass(generateAdapter = true)
data class StreamJson(
    @Json(name = "user_name") val userName: String,
    @Json(name = "title") val title: String,
    @Json(name = "viewer_count") val viewerCount: Long,
    @Json(name = "thumbnail_url") val thumbnailUrl: String,
)

data class StreamUI(
    val userName: String,
    val title: String,
    val viewerCount: Long,
    val thumbnailUrl: String,
)

fun StreamJson.mapToUi(): StreamUI {
    return StreamUI(
        userName = userName,
        title = title,
        viewerCount = viewerCount,
        thumbnailUrl = thumbnailUrl,
    )
}

data class StreamUiState(
    val streams: List<StreamUI> = emptyList(),
    val isLoading: Boolean = false,
    val error: Boolean = false,
) {
    companion object {
        val DEFAULT = StreamUiState()
    }
}
